import { Controller, Get, Post, Body, Patch, Param, Delete } from '@nestjs/common';
import { FoodService } from './food.service';
import { CreateFoodDto } from './dto/request/create-food.dto';
import { ApiOkResponse, ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Food } from './entities/food.entity';
import { UpdateFoodDto } from './dto/request/update-food.dto';
import { ApiUtilResponse } from 'src/type/util.entity';
import { PrismaPromise } from '@prisma/client/runtime/library';
import { Prisma } from '@prisma/client';
import { MessagePattern, Payload } from '@nestjs/microservices';

@Controller('food')
export class FoodController {
  constructor(private readonly foodService: FoodService) {}

  @MessagePattern("get-all-foods")
  findAll() {
    return this.foodService.findAll();
  }

  // @Get(':id')
  // findOne(@Param('id') id: string) {
  //   return this.foodService.findById(id);
  // }
  @MessagePattern("get-food-by-shop-id")
  async findByShopId(@Payload() payload: {shopId:string}): Promise<Food[]> {
    const {shopId} = payload;    
    return this.foodService.findByShopId(shopId);
  }

}
