import { Module } from '@nestjs/common';
import { PaymentService } from './payment-api.service';
import { PaymentApiController } from './payment-api.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { TransactionService } from 'src/transaction-api/transaction-api.service';
import { FoodApiService } from 'src/food-api/food-api.service';
import { RabbitMQService } from 'src/rabbit/rabbit.service';

@Module({
  controllers: [PaymentApiController],
  providers: [PaymentService, TransactionService, FoodApiService, PrismaPostgresService, RabbitMQService],
})
export class PaymentModule {}
