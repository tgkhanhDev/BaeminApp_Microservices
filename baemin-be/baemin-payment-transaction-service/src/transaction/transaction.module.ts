import { Module } from '@nestjs/common';
import { TransactionService } from './transaction.service';
import { TransactionController } from './transaction.controller';
import { PrismaPostgresService } from 'src/prisma/prisma.service';

@Module({
  controllers: [TransactionController],
  providers: [TransactionService, PrismaPostgresService],
})
export class TransactionModule {}
