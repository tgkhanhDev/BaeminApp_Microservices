import { Inject, Injectable } from '@nestjs/common';
import { ClientProxy } from '@nestjs/microservices';
import { CreateTransactionDto, FilterTransactionDto } from './dto/request/create-transaction.dto';
import { lastValueFrom } from 'rxjs';

@Injectable()
export class TransactionApiService {
    constructor(
        @Inject("PAYMENT_API") private transactionService: ClientProxy
    ) { }

    async getTransactionWithFilter(filter: FilterTransactionDto) {
        let res = await lastValueFrom(this.transactionService.send("get-all-transactions", filter));
        return res;
    }

    async createTransaction(req: CreateTransactionDto) {
        let res = await lastValueFrom(this.transactionService.send("create-transaction", req));        
        return res;
    }

   

}
