import { Module } from '@nestjs/common';
import { ShopApiService } from './shop-api.service';
import { ShopApiController } from './shop-api.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { RabbitMQService } from 'src/rabbit/rabbit.service';

@Module({
  controllers: [ShopApiController],
  providers: [ShopApiService, PrismaPostgresService, RabbitMQService],
})
export class ShopModule { }
