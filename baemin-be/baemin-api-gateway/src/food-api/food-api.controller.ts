import { Controller, Get, Inject, Param } from '@nestjs/common';
import { FoodApiService } from './food-api.service';
import { ClientProxy } from '@nestjs/microservices';
import { lastValueFrom } from 'rxjs';
import { ApiOkResponse, ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Food } from './entities/food.entity';

@Controller('food-api')
@ApiTags("Food")
export class FoodApiController {
  constructor(private readonly foodApiService: FoodApiService,
    @Inject("FOOD_API") private foodService: ClientProxy
  ) { }

  @Get('/food')
  @ApiOperation({
    summary: 'Get all food items',
    // description: 'Fetches a list of all available food items from the database',
  })
  @ApiResponse({
    status: 200,
    description: 'Successfully fetched all food items',
    type: [Food],  // Array of Food objects as response
  })
  @ApiResponse({
    status: 500,
    description: 'Internal server error',
  })
  async findAllFoods() {
    let res = await lastValueFrom(this.foodService.send("get-all-foods", ''));
    return res;
  }

  @Get('/food/:shopId')
  @ApiOperation({
    summary: 'Get food by shop id',
  })
  @ApiOkResponse({
    description: 'Successfully fetched food by shop id',
    // type: ApiUtilResponse<Food[]>,
  })
  @ApiResponse({
    status: 500,
    description: 'Internal server error',
  })
  async findAllFoodsByShopId(@Param('shopId') shopId: string): Promise<Food[]> {
    const res = await lastValueFrom(this.foodService.send("get-food-by-shop-id", { shopId }));
    return res;
  }
}
