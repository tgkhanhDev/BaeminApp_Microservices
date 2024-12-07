import { Module } from '@nestjs/common';
import { FoodApiService } from './food-api.service';
import { FoodApiController } from './food-api.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { RabbitMQService } from 'src/rabbit/rabbit.service';

@Module({
  controllers: [FoodApiController],
  providers: [FoodApiService, PrismaPostgresService, RabbitMQService],
})
export class FoodModule { }
