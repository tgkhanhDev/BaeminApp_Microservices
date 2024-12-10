import { Module } from '@nestjs/common';
import { TransactionService } from './transaction-api.service';
import { TransactionApiController } from './transaction-api.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { FoodApiService } from 'src/food-api/food-api.service';
import { RabbitMQService } from 'src/rabbit/rabbit.service';

@Module({
  controllers: [TransactionApiController],
  providers: [TransactionService, FoodApiService, PrismaPostgresService, RabbitMQService],
})
export class TransactionModule {}
