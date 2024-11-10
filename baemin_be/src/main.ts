import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors();

  const config = new DocumentBuilder()
  .setTitle("Baemin Application")
  .setDescription("By tgkhanhDev")
  .setVersion("1.0")
  .addTag('cat')
  .build();

  const document = SwaggerModule.createDocument(app, config);

  SwaggerModule.setup('api-baemin', app, document);
  
  await app.listen(process.env.PORT ?? 8080);
}

bootstrap();
//1yarn prisma init
//2keo vao trong src:
//  yarn db pull --schema src/prisma/<schema-file-name>
//3 yarn prisma generate --schema src/prisma/<schema-file-name>