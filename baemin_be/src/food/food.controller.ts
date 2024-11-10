import { Controller, Get, Post, Body, Patch, Param, Delete } from '@nestjs/common';
import { FoodService } from './food.service';
import { CreateFoodDto } from './dto/request/create-food.dto';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';
import { Food } from './entities/food.entity';
import { UpdateFoodDto } from './dto/request/update-food.dto';

@Controller('food')
@ApiTags("Food")
export class FoodController {
  constructor(private readonly foodService: FoodService) {}

  @Post()
  create(@Body() createFoodDto: CreateFoodDto) {
    return this.foodService.create(createFoodDto);
  }

  @Get()
  @ApiOperation({
    summary: 'Get all food items',
    description: 'Fetches a list of all available food items from the database',
  })
  @ApiResponse({
    status: 200,
    description: 'Successfully fetched all food items',
    type: [Food],  // Array of Food objects as response
  })
  @ApiResponse({
    status: 500,
    description: 'Internal server error',
  })
  findAll() {
    return this.foodService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.foodService.findById(id);
  }

  @Patch(':id')
  update(@Param('id') id: string, @Body() updateFoodDto: UpdateFoodDto) {
    return this.foodService.update(+id, updateFoodDto);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.foodService.remove(+id);
  }
}
