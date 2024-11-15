import { transaction_status } from '@prisma/client';  // Importing enum from Prisma

export class TransactionDto {
    transaction_id: string;

    food_id: string;

    quantity: number;

    per_price: number;

    payment_id?: string;

    status: transaction_status;
}