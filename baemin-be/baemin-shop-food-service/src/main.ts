import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { MicroserviceOptions, Transport } from '@nestjs/microservices';

async function bootstrap() {
  const app = await NestFactory.createMicroservice<MicroserviceOptions>(AppModule, {
    transport: Transport.RMQ,
    options: {
      urls: ['amqp://admin:170504@localhost:5672'],
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
//1yarn prisma init
//2keo vao trong src:
//  yarn db pull --schema src/prisma/<schema-file-name>
//3 yarn prisma generate --schema src/prisma/<schema-file-name>