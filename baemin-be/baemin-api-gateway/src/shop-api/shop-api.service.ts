import { Inject, Injectable } from '@nestjs/common';
import { ClientProxy } from '@nestjs/microservices';
import { lastValueFrom } from 'rxjs';
import { ShopResponseDetailDto, ShopResponseDto } from './dto/shops-response.dto';
import { ShopFilterRequestDto } from './dto/shop-request.dto';

@Injectable()
export class ShopApiService {
    constructor(
        @Inject("SHOP_API") private shopService: ClientProxy
    ) { }

    async findAllShopsFilter(filter: ShopFilterRequestDto): Promise<ShopResponseDto[]> {
        let res = await lastValueFrom(this.shopService.send("get-all-shops-filter", filter));
        return res;
    }

    async findShopDetailById(shop_id: string): Promise<ShopResponseDetailDto> {
        let res = await lastValueFrom(this.shopService.send("get-shop-detail-by-id", shop_id));
        return res;
    }

}
