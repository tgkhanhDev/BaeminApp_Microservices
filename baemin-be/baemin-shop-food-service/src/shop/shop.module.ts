import { Module } from '@nestjs/common';
import { ShopService } from './shop.service';
import { ShopController } from './shop.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';

@Module({
  controllers: [ShopController],
  providers: [ShopService, PrismaPostgresService],
})
export class ShopModule {}
