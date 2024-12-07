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

  //*=================
  // const app = await NestFactory.create(AppModule);
  // app.enableCors();
  // app.use((err, req, res, next) => {
  //   const statusCode = err.status || 500;
  //   const message = err.message || "Internal Server Error";
  //   res.status(statusCode).json({
  //     statusCode,
  //     message
  //   });
  // });

  // const config = new DocumentBuilder()
  //   .setTitle("Baemin Application")
  //   .setDescription("By tgkhanhDev")
  //   .setVersion("1.0")
  //   .build();

  // const document = SwaggerModule.createDocument(app, config);

  // SwaggerModule.setup('api-baemin', app, document);
  
  // await app.listen(8080);
  //*=================
}
bootstrap();
