import { Module } from '@nestjs/common';
import { FoodService } from './food.service';
import { FoodController } from './food.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';

@Module({
  controllers: [FoodController],
  providers: [FoodService, PrismaPostgresService],
})
export class FoodModule {}
