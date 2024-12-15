import { Controller, Get, Inject, OnModuleInit, Param } from '@nestjs/common';
import { FoodApiService } from './food-api.service';
import { lastValueFrom } from 'rxjs';
import { ApiOkResponse, ApiOperation, ApiParam, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Food } from './entities/food.entity';
import { Ctx, MessagePattern, Payload, RmqContext } from '@nestjs/microservices';
import { RabbitMQService } from 'src/rabbit/rabbit.service';
import { ConsumeMessage } from 'amqplib';
import { CacheManagerService } from 'src/cache-manager/cache-manager.service';
import { CACHE_MANAGER } from '@nestjs/cache-manager';
import { Cache } from 'cache-manager';

export class FoodApiController implements OnModuleInit {

  private readonly foodQueue = 'nest_food_service';

  constructor(
    private readonly foodApiService: FoodApiService,
    private rabbitMQService: RabbitMQService,
    @Inject(CACHE_MANAGER) private cacheManager: Cache
  ) { }

  async onModuleInit() {
    // Listen to the queue
    this.rabbitMQService.consume(this.foodQueue, this.foodQueueProducer.bind(this));
  }

  private async foodQueueProducer(msg: ConsumeMessage) {
    if (msg) {
      const routingKey = msg.fields.routingKey;
      let res = null;
      if (routingKey == 'food.get-all') {
        res = await this.foodApiService.findAll();
      } else if (routingKey == 'food.get-by-id') {
        //{foodId} : string
        //! no co kieur "U-U-D-I" ??:D
        let foodId = msg.content.toString().replace(/^"|"$/g, '');
        res = await this.foodApiService.findById(foodId);
      } else if (routingKey == 'food.get-all-by-shop-id') {
        let shopId = msg.content.toString().replace(/^"|"$/g, '');
        res = await this.foodApiService.findByShopId(shopId);
      }
      // //TODO: CACHE TEST
      else if (routingKey == 'food.get-cache-by-key') {
        let key = msg.content.toString().replace(/^"|"$/g, '');
        console.log("keyb4: ", key);
        
        res = await this.cacheManager.get(key);
         
        if (res) {
          try {
            // Try to parse as JSON (for objects stored as JSON)
            res = JSON.parse(res);
            // console.log("resGetKey: ", res);
          } catch (error) {
            // If parsing fails, assume it's a plain string
            // res = res; console.log("res: ", res);

          }
        } else {
          res = undefined; // Handle case where cache is empty
        }

        res = res == undefined ? "Not found "+key : res;

      } else if (routingKey == 'food.add-cache') {
        let req = JSON.parse(msg.content ? msg.content.toString() : '{}');
        let payload: any = "{}";
        
        if (req != null) {
          payload = await this.rabbitMQService.parseJsonToDto(req, ObjectCacheBody)
        }
        
        // console.log("reqGETCACHE: ", req, '=>>>>', payload);

        await this.cacheManager.set(payload.key, payload.value);
        res = 'Set '+payload.key+' With value: ' + payload.value;
      } else if (routingKey == 'food.delete-cache-by-key'){
        let key = msg.content.toString().replace(/^"|"$/g, '');
        await this.cacheManager.del(key);
        res = 'Deleted '+key;
      }

      this.rabbitMQService.sendResponse(msg, res);
    }
  }
}

class ObjectCacheBody {
  key: string;
  value: string;
}