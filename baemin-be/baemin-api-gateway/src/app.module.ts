import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { FoodApiModule } from './food-api/food-api.module';
import { ShopApiModule } from './shop-api/shop-api.module';

@Module({
  imports: [FoodApiModule, ShopApiModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
