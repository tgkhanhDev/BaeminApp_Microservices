import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { PrismaModule } from './prisma/prisma.module';
import { FoodModule } from './food-api/food-api.module';
import { ConfigModule } from '@nestjs/config';
import { ShopModule } from './shop-api/shop-api.module';
import { CartModule } from './cart-api/cart-api.module';
import { RabbitMQService } from './rabbit/rabbit.service';
import { TransactionModule } from './transaction-api/transaction-api.module';
import { PaymentModule } from './payment-api/payment-api.module';

@Module({
  imports: [PrismaModule, FoodModule, ShopModule, CartModule, TransactionModule, PaymentModule, ConfigModule.forRoot({ isGlobal: true })],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
