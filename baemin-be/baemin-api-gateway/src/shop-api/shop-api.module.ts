import { Module } from '@nestjs/common';
import { ShopApiService } from './shop-api.service';
import { ShopApiController } from './shop-api.controller';
import { ClientsModule, Transport } from '@nestjs/microservices';

@Module({
  imports: [
    ClientsModule.register(
      [
        {
          name: 'SHOP_API',
          transport: Transport.RMQ,
          options: {
            urls: ['amqp://admin:170504@trangiangkhanh.site:5672'],
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
  controllers: [ShopApiController],
  providers: [ShopApiService],
})
export class ShopApiModule {}
