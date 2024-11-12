import { Controller, Get, Post, Body, Patch, Param, Delete } from '@nestjs/common';
import { ShopService } from './shop.service';
import { CreateShopDto } from './dto/create-shop.dto';
import { UpdateShopDto } from './dto/update-shop.dto';
import { ShopResponseDto } from './dto/shops-response.dto';
import { MessagePattern, Payload } from '@nestjs/microservices';
import { ShopFilterRequestDto } from './dto/shop-request.dto';

@Controller('shop')
export class ShopController {
  constructor(private readonly shopService: ShopService) {}

  @MessagePattern("get-all-shops-filter")
  findByShopLabel(@Payload() filter: ShopFilterRequestDto): Promise<ShopResponseDto[]> {
    return this.shopService.findAllShopFilter(filter);
  }
  
  @MessagePattern("get-shop-detail-by-id")
  findShopById(shop_id: string): Promise<any> { 
    return this.shopService.findShopById(shop_id);
  }

}
