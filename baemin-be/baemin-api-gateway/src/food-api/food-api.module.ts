import { Module } from '@nestjs/common';
import { FoodApiService } from './food-api.service';
import { FoodApiController } from './food-api.controller';
import { ClientsModule, Transport } from '@nestjs/microservices';

@Module({
  imports: [
    ClientsModule.register(
      [
        {
          name: 'FOOD_API',
          transport: Transport.RMQ,
          options: {
            urls: ['amqp://admin:170504@localhost:5672'],
            queue: 'shop_food_queue',
            queueOptions: {
              durable: true
            },
            persistent: true
          }
        }
      ]
    )
  ],
  controllers: [FoodApiController],
  providers: [FoodApiService],
})
export class FoodApiModule { }
