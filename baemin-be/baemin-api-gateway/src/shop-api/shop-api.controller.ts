import { Controller, Get } from '@nestjs/common';
import { ShopApiService } from './shop-api.service';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { lastValueFrom } from 'rxjs';
import { ShopResponseDto } from './dto/shops-response.dto';

@Controller('shop-api')
@ApiTags("Shop")
export class ShopApiController {
  constructor(private readonly shopApiService: ShopApiService) { }

  @Get('')
  @ApiOperation({
    summary: 'Get all shops',
    // description: 'Fetches a list of all available food items from the database',
  })
  @ApiResponse({
    status: 200,
    description: 'Successfully fetched all food items',
    type: [ShopResponseDto],  
  })
  @ApiResponse({
    status: 500,
    description: 'Internal server error',
  })
  async findAllShops(): Promise<ShopResponseDto[]> {
    let res = await this.shopApiService.findAllShops();
    return res;
  }
}
