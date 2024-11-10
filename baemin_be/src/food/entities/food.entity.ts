import { ApiProperty } from "@nestjs/swagger";
import { Shop } from "src/shop/entities/shop.entity";

export enum FoodType {
    combo = 'combo',
    sale = 'sale',
    rice_chicken = 'rice chicken', // Map to the same name in Prisma
    bubble_tea = 'bubble tea', // Map to the same name in Prisma
}

export class Food {
    @ApiProperty({ description: 'Unique identifier for the food item', example: 'e58f1b28-470e-485a-bc56-50108d08e197' })
    food_id: string;
  
    @ApiProperty({ description: 'Name of the food item', example: 'Bubble Tea' })
    food_name: string;
  
    @ApiProperty({ description: 'Description of the food item', example: 'A refreshing bubble tea with tapioca pearls.' })
    description: string;
  
    @ApiProperty({ description: 'Price of the food item', example: 50000 })
    price: number;
  
    @ApiProperty({ enum: FoodType, description: 'Type of the food item', example: FoodType.bubble_tea })
    type: FoodType;
  
    @ApiProperty({ description: 'The shop where this food item is available', type: Shop, example: {} })
    Shop?: Shop; // Assuming the Shop entity has already been defined
  
    @ApiProperty({ description: 'Unique identifier for the shop that owns the food item', example: 'd2fa29f2-3064-43d5-b5c6-810b989b2e56' })
    shop_id?: string;
  }
