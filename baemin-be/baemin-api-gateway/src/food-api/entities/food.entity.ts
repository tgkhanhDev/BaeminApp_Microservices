import { ApiProperty } from "@nestjs/swagger";

export enum FoodType {
    combo = 'combo',
    sale = 'sale',
    rice_chicken = 'rice chicken',
    bubble_tea = 'bubble tea',
    none = 'none',
}

export class Food {
    @ApiProperty({ example: 'e58f1b28-470e-485a-bc56-50108d08e197' })
    food_id: string;

    @ApiProperty({ example: 'Bubble Tea' })
    food_name: string;

    @ApiProperty({ example: 'A refreshing bubble tea with tapioca pearls.', nullable: true })
    description: string | null;

    @ApiProperty({ example: 50000 })
    price: number;

    @ApiProperty({ enum: FoodType, example: FoodType.bubble_tea })
    type: FoodType;

    @ApiProperty({ example: 'd2fa29f2-3064-43d5-b5c6-810b989b2e56', nullable: true })
    shop_id: string | null;
}
