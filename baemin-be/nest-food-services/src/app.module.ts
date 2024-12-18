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
import { CacheManagerService } from './cache-manager/cache-manager.service';
import { CacheManagerModule } from './cache-manager/cache-manager.module';
import { ElasticModule } from './elastic/elastic.module';

@Module({
  imports: [PrismaModule, FoodModule, ShopModule, CartModule, TransactionModule, PaymentModule, ConfigModule.forRoot({ isGlobal: true }), CacheManagerModule, ElasticModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
