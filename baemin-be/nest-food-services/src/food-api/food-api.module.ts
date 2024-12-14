import { Module } from '@nestjs/common';
import { FoodApiService } from './food-api.service';
import { FoodApiController } from './food-api.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { RabbitMQService } from 'src/rabbit/rabbit.service';
import { CacheManagerService } from 'src/cache-manager/cache-manager.service';
import { CacheManagerModule } from 'src/cache-manager/cache-manager.module';

@Module({
  imports: [CacheManagerModule],
  controllers: [FoodApiController],
  providers: [FoodApiService, PrismaPostgresService, RabbitMQService],
})
export class FoodModule { }
