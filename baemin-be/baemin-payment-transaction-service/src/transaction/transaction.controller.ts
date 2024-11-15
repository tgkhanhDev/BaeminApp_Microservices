import { Controller } from '@nestjs/common';
import { TransactionService } from './transaction.service';
import { CreateTransactionDto, FilterTransactionDto } from './dto/request/create-transaction.dto';
import { MessagePattern } from '@nestjs/microservices';
import { ResponseTransactionDto } from './dto/response/response-transaction.dto';

@Controller('transaction')
export class TransactionController {
  constructor(private readonly transactionService: TransactionService) {}

  @MessagePattern('get-all-transactions')
  getAllTransactionsWithFilter(filter: FilterTransactionDto): Promise<ResponseTransactionDto[]> {
    return this.transactionService.getTransactionsWithFilter(filter);
  }

  @MessagePattern('create-transaction')
  createTransaction(req: CreateTransactionDto) {
    return this.transactionService.createTransaction(req);
  }
}
