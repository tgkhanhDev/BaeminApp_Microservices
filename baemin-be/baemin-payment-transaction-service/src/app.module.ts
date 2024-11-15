import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TransactionModule } from './transaction/transaction.module';
import { PrismaPostgresService } from './prisma/prisma.service';
import { PaymentModule } from './payment/payment.module';

@Module({
  imports: [TransactionModule, PaymentModule],
  controllers: [AppController],
  providers: [AppService, PrismaPostgresService],
})
export class AppModule {}
