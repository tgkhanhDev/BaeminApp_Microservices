import { Module } from '@nestjs/common';
import { TransactionApiService } from './transaction-api.service';
import { TransactionApiController } from './transaction-api.controller';
import { ClientsModule, Transport } from '@nestjs/microservices';

@Module({
  imports: [
    ClientsModule.register(
      [
        {
          name: 'PAYMENT_API',
          transport: Transport.RMQ,
          options: {
            urls: ['amqp://admin:170504@trangiangkhanh.site:5672'],
            queue: 'payment_queue',
            queueOptions: {
              durable: true
            },
            persistent: true
          }
        }
      ]
    )
  ],
  controllers: [TransactionApiController],
  providers: [TransactionApiService],
})
export class TransactionApiModule {}
