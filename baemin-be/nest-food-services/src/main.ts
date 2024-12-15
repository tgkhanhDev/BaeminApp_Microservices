import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { MicroserviceOptions, Transport } from '@nestjs/microservices';

async function bootstrap() {
  
  const app = await NestFactory.createMicroservice<MicroserviceOptions>(AppModule, {
    transport: Transport.RMQ,
    options: {
      urls: ['amqp://admin:170504@localhost:5672'],
      headers:{
        // "x-dead-letter-exchange": "food_queue",
        // "x-dead-letter-routing-key": "food_queue"
        // "endpoint": 'foodApi'
      },
      persistent: true
    }
  });
  await app.listen();
}
bootstrap();
