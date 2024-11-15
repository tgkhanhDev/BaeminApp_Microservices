import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { MicroserviceOptions, Transport } from '@nestjs/microservices';

async function bootstrap() {
  const app = await NestFactory.createMicroservice<MicroserviceOptions>(AppModule, {
    transport: Transport.RMQ,
    options: {
      urls: ['amqp://admin:170504@trangiangkhanh.site:5672'],
      queue: "shop_food_queue",
      queueOptions: {
        durable: true 
      },
      persistent: true
    }
  });
  await app.listen();
}

bootstrap();
//0 yarn add prisma @prisma/client
//1 yarn prisma init
//2keo vao trong src:
//  yarn prisma db pull --schema src/prisma/<schema-file-name>
//3 yarn prisma generate --schema src/prisma/<schema-file-name>