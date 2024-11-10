import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { PrismaModule } from './prisma/prisma.module';
import { FoodModule } from './food/food.module';
import { ShopModule } from './shop/shop.module';

@Module({
  imports: [PrismaModule, FoodModule, ShopModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
