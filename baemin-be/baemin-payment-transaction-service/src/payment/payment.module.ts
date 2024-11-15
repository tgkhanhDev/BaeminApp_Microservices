import { Module } from '@nestjs/common';
import { PaymentService } from './payment.service';
import { PaymentController } from './payment.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';

@Module({
  controllers: [PaymentController],
  providers: [PaymentService, PrismaPostgresService],
})
export class PaymentModule {}
