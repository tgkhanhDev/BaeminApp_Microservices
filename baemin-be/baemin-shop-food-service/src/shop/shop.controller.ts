import { Controller, Get, Post, Body, Patch, Param, Delete } from '@nestjs/common';
import { ShopService } from './shop.service';
import { CreateShopDto } from './dto/create-shop.dto';
import { UpdateShopDto } from './dto/update-shop.dto';
import { ShopResponseDto } from './dto/shops-response.dto';
import { MessagePattern } from '@nestjs/microservices';

@Controller('shop')
export class ShopController {
  constructor(private readonly shopService: ShopService) {}

  @MessagePattern("get-all-shops")
  findAll(): Promise<ShopResponseDto[]> {
    return this.shopService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.shopService.findOne(+id);
  }

}
