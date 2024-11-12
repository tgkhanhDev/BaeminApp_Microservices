import { Controller, Get, Query } from '@nestjs/common';
import { ShopApiService } from './shop-api.service';
import { ApiOperation, ApiQuery, ApiResponse, ApiTags } from '@nestjs/swagger';
import { lastValueFrom } from 'rxjs';
import { ShopResponseDto } from './dto/shops-response.dto';
import { ShopLocation, ShopLabel } from './entities/shop.entity';
import { ShopFilterRequestDto } from './dto/shop-request.dto';

@Controller('shop-api')
@ApiTags("Shop")
export class ShopApiController {
  constructor(private readonly shopApiService: ShopApiService) { }

  @Get('')
  @ApiOperation({
    summary: 'Get all shops by label',
  })
  @ApiQuery({
    name: 'label',
    enum: ShopLabel,
    example: ShopLabel.FOOD,
    required: false,
  })
  @ApiQuery({
    name: 'location',
    enum: ShopLocation,
    example: ShopLocation.Ho_Chi_Minh,
    required: false,
  })
  @ApiQuery({
    name: 'name',
    example: "Gà Ủ Muối",
    required: false,
  })
  @ApiResponse({
    status: 200,
    description: 'Successfully fetched all food items',
    type: [ShopResponseDto],
  })
  async findAllShops(@Query() filter: ShopFilterRequestDto): Promise<ShopResponseDto[]> {
    const res = await this.shopApiService.findAllShopsFilter(filter);
    return res;
  }

}
