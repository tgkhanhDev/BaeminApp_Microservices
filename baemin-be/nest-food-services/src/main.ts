import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { MicroserviceOptions, Transport } from '@nestjs/microservices';
import { WinstonModule } from 'nest-winston';
import * as winston from 'winston';
import { ArgumentsHost, Catch, ExceptionFilter, HttpException, Logger } from '@nestjs/common';

async function bootstrap() {

  //Log collector
  const logger = WinstonModule.createLogger({
    // format: winston.format.combine(
    //   winston.format.errors({ stack: true }),
    //   winston.format.json()
    // ),
    defaultMeta: { service: "Nest_Food_Services", type: "log_collector" }, // Add type here
    transports: [
      new winston.transports.Http({
        host: 'logstash',
        port: 5044,
        level: 'error',
      }),
      new winston.transports.Console(),
      // new winston.transports.File({ filename: 'logs/nest-food.log', level: 'error' }),
    ],
  });

  const app = await NestFactory.createMicroservice<MicroserviceOptions>(AppModule, {
    logger, //* Winston Style
    transport: Transport.RMQ,
    options: {
      urls: ['amqp://admin:170504@localhost:5672'],
      headers: {
        // "x-dead-letter-exchange": "food_queue",
        // "x-dead-letter-routing-key": "food_queue"
        // "endpoint": 'foodApi'
      },
      persistent: true
    }
  });

  app.useGlobalFilters(new AllExceptionsFilter());
  await app.listen();
}
// Logger.error("")
bootstrap();


@Catch()
export class AllExceptionsFilter implements ExceptionFilter {
  private readonly logger = new Logger(AllExceptionsFilter.name);

  catch(exception: unknown, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse();
    const request = ctx.getRequest();

    const status =
      exception instanceof HttpException
        ? exception.getStatus()
        : 500;

    const message =
      exception instanceof HttpException
        ? exception.getResponse()
        : exception;

    this.logger.error(`Exception: ${JSON.stringify(message)}`);

    response.status(status).json({
      statusCode: status,
      timestamp: new Date().toISOString(),
      path: request.url,
    });
  }
}