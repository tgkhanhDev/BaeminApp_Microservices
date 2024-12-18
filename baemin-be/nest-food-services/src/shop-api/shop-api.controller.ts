import { Controller, Get, OnModuleInit, Param, Query } from '@nestjs/common';
import { ShopApiService } from './shop-api.service';
import { ApiOperation, ApiParam, ApiQuery, ApiResponse, ApiTags } from '@nestjs/swagger';
import { lastValueFrom } from 'rxjs';
import { ShopResponseDetailDto, ShopResponseDto } from './dto/shops-response.dto';
import { ShopLocation, ShopLabel } from './entities/shop.entity';
import { ShopFilterRequestDto } from './dto/shop-request.dto';
import { RabbitMQService } from 'src/rabbit/rabbit.service';
import { ConsumeMessage } from 'amqplib';
import { plainToInstance } from 'class-transformer';
import { validate } from 'class-validator';

@Controller('shop-api')
@ApiTags("Shop")
export class ShopApiController implements OnModuleInit {

  private readonly shopQueue = 'nest_shop_service';

  constructor(
    private readonly shopApiService: ShopApiService,
    private rabbitMQService: RabbitMQService
  ) { }

  async onModuleInit() {
    // Listen to the queue
    this.rabbitMQService.consume(this.shopQueue, this.shopQueueProducer.bind(this));
  }

  private async shopQueueProducer(msg: ConsumeMessage) {
    if (msg) {
      const routingKey = msg.fields.routingKey;
      let res = null;
      if (routingKey == 'shop.get-all-filtered') {
        const shopFilterRequestDto = JSON.parse(msg.content ?  msg.content.toString() : '{}');
        let payload: any = "{}";
        if(shopFilterRequestDto != null){
          payload = await this.parseJsonToDto(shopFilterRequestDto, ShopFilterRequestDto)
        }
        console.log("shopFilterRequestDto: ", shopFilterRequestDto, '=>>>>', payload);
        

        res = await this.shopApiService.findAllShopFilter(payload);
      } else if (routingKey == 'shop.get-by-id') {
        //{foodId} : string
        //! no co kieur "U-U-D-I" ??:D
        let shopId = msg.content.toString().replace(/^"|"$/g, '');
        res = await this.shopApiService.findShopById(shopId);
      }
      // console.log("resShop: ", res);

      this.rabbitMQService.sendResponse(msg, res);

    }
  }


  async parseJsonToDto<T>(jsonString: string, dtoClass: new () => T): Promise<T | null> {
    try {
      // Parse the JSON string
      const plainObject = JSON.parse(jsonString);

      // Transform to DTO instance
      const dtoInstance = plainToInstance(dtoClass, plainObject);

      // Validate the DTO instance
      const errors = await validate(dtoInstance as object);

      if (errors.length > 0) {
        console.error('Validation errors:', errors);
        return null; // Return null if validation fails
      }

      return dtoInstance; // Return the valid DTO instance
    } catch (error) {
      console.error('Error parsing JSON:', error);
      return null; // Return null if parsing fails
    }
  }

  // @Get('')
  // @ApiOperation({
  //   summary: 'Get all shops by label',
  // })
  // @ApiQuery({
  //   name: 'label',
  //   enum: ShopLabel,
  //   example: ShopLabel.FOOD,
  //   required: false,
  // })
  // @ApiQuery({
  //   name: 'location',
  //   enum: ShopLocation,
  //   example: ShopLocation.Ho_Chi_Minh,
  //   required: false,
  // })
  // @ApiQuery({
  //   name: 'name',
  //   example: "Dessert Kingdom",
  //   required: false,
  // })
  // @ApiResponse({
  //   status: 200,
  //   description: 'Successfully fetched all food items',
  //   type: [ShopResponseDto],
  // })
  // async findAllShops(@Query() filter: ShopFilterRequestDto): Promise<ShopResponseDto[]> {
  //   const res = await this.shopApiService.findAllShopFilter(filter);
  //   return res;
  // }

  // @Get('/:shop_id')
  // @ApiOperation({
  //   summary: 'Get shop details by ID',
  // })
  // @ApiParam({
  //   name: 'shop_id',
  //   description: 'ID of the shop to retrieve details for',
  //   required: true,
  //   example: '4e69b389-9d4b-401a-943d-936bbde62f1a',
  // })
  // @ApiResponse({
  //   status: 200,
  //   description: 'Successfully fetched shop details',
  //   type: ShopResponseDetailDto,
  // })
  // async findShopDetailById(@Param('shop_id') shop_id: string): Promise<ShopResponseDetailDto> {
  //   const res = await this.shopApiService.findShopById(shop_id);
  //   return res;
  // }

}
