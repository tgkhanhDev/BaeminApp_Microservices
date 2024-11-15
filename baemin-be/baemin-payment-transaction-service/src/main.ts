import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { MicroserviceOptions, Transport } from '@nestjs/microservices';

async function bootstrap() {
  const app = await NestFactory.createMicroservice<MicroserviceOptions>(AppModule, {
    transport: Transport.RMQ,
    options: {
      urls: ['amqp://admin:170504@trangiangkhanh.site:5672'],
      queue: "payment_queue",
      queueOptions: {
        durable: true
      },
      persistent: true
    }
  });
  
  await app.listen();
}
bootstrap();
