import { Inject, Injectable } from '@nestjs/common';
import { ClientProxy } from '@nestjs/microservices';
import { lastValueFrom } from 'rxjs';
import { ShopResponseDto } from './dto/shops-response.dto';
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

}
