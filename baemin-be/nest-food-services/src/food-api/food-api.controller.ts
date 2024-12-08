import { Controller, Get, Inject, OnModuleInit, Param } from '@nestjs/common';
import { FoodApiService } from './food-api.service';
import { lastValueFrom } from 'rxjs';
import { ApiOkResponse, ApiOperation, ApiParam, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Food } from './entities/food.entity';
import { Ctx, MessagePattern, Payload, RmqContext } from '@nestjs/microservices';
import { RabbitMQService } from 'src/rabbit/rabbit.service';
import { ConsumeMessage } from 'amqplib';

@Controller('food-api')
@ApiTags("Food")
export class FoodApiController implements OnModuleInit {

  private readonly foodQueue = 'nest_food_service';

  constructor(
    private readonly foodApiService: FoodApiService,
    private rabbitMQService: RabbitMQService
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
      } else if (routingKey == 'food.get-by-id'){
        //{foodId} : string
        //! no co kieur "U-U-D-I" ??:D
        let foodId = msg.content.toString().replace(/^"|"$/g, ''); 
        res = await this.foodApiService.findById(foodId);
      } else if (routingKey == 'food.get-all-by-shop-id'){
        let shopId = msg.content.toString().replace(/^"|"$/g, ''); 
        res = await this.foodApiService.findByShopId(shopId);
      }
      this.rabbitMQService.sendResponse(msg, res);

    }
  }

  // async findAllFoodsConsumer(msg: ConsumeMessage) {
  //   //get Data
  //   // return res;
  // }

  // @MessagePattern("get-all-foods")
  // async findAllFoods() {
  //   let res = await this.foodApiService.findAll();
  //   return res;
  // }

  // @MessagePattern('get-all-foods') // Match the routing key
  // async findAllFoods1(@Payload() data: any, @Ctx() context: RmqContext) {
  //   const channel = context.getChannelRef();
  //   const message = context.getMessage();

  //   console.log('Received Message:', data);

  //   // Process the message
  //   const foods = await this.foodApiService.findAll();

  //   // Acknowledge the message
  //   channel.ack(message);

  //   return foods; // This response will be sent back to the producer
  // }


  // @Get('/food/:foodId')
  // @ApiOperation({
  //   summary: 'Get all food items',
  //   // description: 'Fetches a list of all available food items from the database',
  // })
  // @ApiParam({
  //   name: 'foodId',
  //   required: true,
  //   type: String
  // })
  // @ApiResponse({
  //   status: 200,
  //   description: 'Successfully fetched all food items',
  //   type: [Food],  // Array of Food objects as response
  // })
  // @ApiResponse({
  //   status: 500,
  //   description: 'Internal server error',
  // })
  // async findFoodById(@Param('foodId') foodId: string) {
  //   let res = await this.foodApiService.findById(foodId);
  //   return res;
  // }

  // @Get('/food/:shopId')
  // @ApiOperation({
  //   summary: 'Get food by shop id',
  // })
  // @ApiOkResponse({
  //   description: 'Successfully fetched food by shop id',
  //   // type: ApiUtilResponse<Food[]>,
  // })
  // @ApiResponse({
  //   status: 500,
  //   description: 'Internal server error',
  // })
  // async findAllFoodsByShopId(@Param('shopId') shopId: string): Promise<Food[]> {
  //   const res = await this.foodApiService.findByShopId(shopId);
  //   return res;
  // }
}
