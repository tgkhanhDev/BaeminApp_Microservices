import { Injectable } from '@nestjs/common';
import { CreateFoodDto } from './dto/request/create-food.dto';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { UpdateFoodDto } from './dto/request/update-food.dto';
import { Food, FoodType } from './entities/food.entity';

import { Prisma, food_type as PrismaFoodType } from '.prismas/client-postgres';

function mapFoodType(prismaType: PrismaFoodType): FoodType {
  switch (prismaType) {
    case 'combo':
      return FoodType.combo;
    case 'sale':
      return FoodType.sale;
    case 'rice_chicken':
      return FoodType.rice_chicken;
    case 'bubble_tea':
      return FoodType.bubble_tea;
    default:
      return FoodType.none;
  }
}


@Injectable()
export class FoodService {

  constructor(
    private postgresDAO: PrismaPostgresService
  ){}

  create(createFoodDto: CreateFoodDto) {
    return 'This action adds a new food';
  }

  findAll() {
    return this.postgresDAO.food.findMany();
  }                     

  findById(id: string) {
    return this.postgresDAO.food.findFirstOrThrow({
      where: {
        food_id: id
      }
    });
  }

  async findByShopId(shop_id: string): Promise<Food[]> {
    const foods = await this.postgresDAO.food.findMany({
      where: {
        shop_id
      }
    });
    return foods.map(food => ({
      food_id: food.food_id,
      food_name: food.food_name,
      description: food.description,
      price: food.price,
      type: mapFoodType(food.type),
      shop_id: food.shop_id
    }));;
  }

  update(id: number, updateFoodDto: UpdateFoodDto) {
    return `This action updates a #${id} food`;
  }

  remove(id: number) {
    return `This action removes a #${id} food`;
  }
}
