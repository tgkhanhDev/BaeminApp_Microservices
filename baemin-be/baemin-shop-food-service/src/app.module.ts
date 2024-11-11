import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { PrismaModule } from './prisma/prisma.module';
import { FoodModule } from './food/food.module';
import { ShopModule } from './shop/shop.module';
import { ConfigModule } from '@nestjs/config';

@Module({
  imports: [PrismaModule, FoodModule, ShopModule, ConfigModule.forRoot({ isGlobal: true })],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
