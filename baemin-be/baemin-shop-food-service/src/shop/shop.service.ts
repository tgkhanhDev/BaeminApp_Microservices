import { Injectable } from '@nestjs/common';
import { CreateShopDto } from './dto/create-shop.dto';
import { UpdateShopDto } from './dto/update-shop.dto';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { ShopResponseDto } from './dto/shops-response.dto';
import { ShopFilterRequestDto } from './dto/shop-request.dto';

@Injectable()
export class ShopService {

  constructor(
    private postgresDAO: PrismaPostgresService
  ) { }

  findAllShopFilter(shopFilterRequestDto: ShopFilterRequestDto): Promise<ShopResponseDto[]> {

    const { label, location, name } = shopFilterRequestDto;

    return this.postgresDAO.shop.findMany({
      where: {
        ...(label ? { label } : {}),  // Include 'label' only if it is not empty or undefined
        ...(location ? { location } : {}), // Include 'location' only if it is not empty or undefined
        ...(name ? { shop_name: { contains: name } } : {}), // Include 'name' only if it is not empty or undefined
      },
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

}
