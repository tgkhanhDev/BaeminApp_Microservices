import { Inject, Injectable } from '@nestjs/common';
import { ClientProxy } from '@nestjs/microservices';
import { lastValueFrom } from 'rxjs';
import { ShopResponseDto } from './dto/shops-response.dto';

@Injectable()
export class ShopApiService {
    constructor(
        @Inject("SHOP_API") private shopService: ClientProxy
    ) { }

    async findAllShops(): Promise<ShopResponseDto[]> {
        let res = await lastValueFrom(this.shopService.send("get-all-shops", ''));
        return res;
    }
}
