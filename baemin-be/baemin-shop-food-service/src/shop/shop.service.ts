import { Injectable } from '@nestjs/common';
import { CreateShopDto } from './dto/create-shop.dto';
import { UpdateShopDto } from './dto/update-shop.dto';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { ShopResponseDto } from './dto/shops-response.dto';

@Injectable()
export class ShopService {

  constructor(
    private postgresDAO: PrismaPostgresService
  ) { }

  findAll(): Promise<ShopResponseDto[]> {
    return this.postgresDAO.shop.findMany({
      select: {
        shop_id: true,
        shop_name: true,
        shop_address: true,
        shop_thumbnail: true,
        category: true,
        label: true,
        location: true,
        rating: true,
        open_time: true,
        close_time: true,
        price_start: true,
        price_end: true,
        is_open: true,
      },
    });
  }

  findOne(id: number) {
    return `This action returns a #${id} shop`;
  }

}
