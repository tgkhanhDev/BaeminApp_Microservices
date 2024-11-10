import { Injectable } from '@nestjs/common';
import { CreateFoodDto } from './dto/request/create-food.dto';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { UpdateFoodDto } from './dto/request/update-food.dto';

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

  update(id: number, updateFoodDto: UpdateFoodDto) {
    return `This action updates a #${id} food`;
  }

  remove(id: number) {
    return `This action removes a #${id} food`;
  }
}
