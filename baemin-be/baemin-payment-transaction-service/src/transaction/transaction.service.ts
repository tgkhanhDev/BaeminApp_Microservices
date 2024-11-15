import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { CreateTransactionDto, FilterTransactionDto } from './dto/request/create-transaction.dto';
import { ResponseTransactionDto } from './dto/response/response-transaction.dto';

class ErrorResponse {
    constructor(public message: string, public code: number) { }
}

@Injectable()
export class TransactionService {

    constructor(
        private postgresDAO: PrismaPostgresService
    ) { }

    async createTransaction(req: CreateTransactionDto): Promise<any> {

        const { food_id, quantity, per_price, payment_id, status } = req;

        const isPaymentExists = payment_id && await this.postgresDAO.payment.findUnique({
            where: { payment_id: payment_id },
        });

        if (payment_id && !isPaymentExists) {
            throw new HttpException("Payment ID does not exist.", HttpStatus.NOT_FOUND);
        }

        return this.postgresDAO.transaction.create({
            data: {
                food_id: food_id,
                quantity: quantity,
                per_price: per_price,
                payment_id: payment_id || null,
                status: status
            }
        });
    }

    getTransactionsWithFilter(filter: FilterTransactionDto): Promise<ResponseTransactionDto[]> {

        const { transaction_id, payment_id, status } = filter

        return this.postgresDAO.transaction.findMany({
            where: {
                ...(transaction_id ? { transaction_id } : {}),
                ...(payment_id ? { payment_id } : {}),
                ...(status ? { status } : {})
            },
            select: {
                food_id: true,
                quantity: true,
                per_price: true,
                status: true,
                transaction_id: true,
                Payment: {
                    select: {
                        payment_id: true,
                        delivery_address: true,
                        message: true,
                        status: true,
                        total_cost: true,
                        Transaction: false
                    }
                },
            }
        })
    }
}
