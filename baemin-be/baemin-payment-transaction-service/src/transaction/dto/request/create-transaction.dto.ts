import { transaction_status } from "@prisma/client";

export class CreateTransactionDto {
    food_id?: string;

    quantity: number;

    per_price: number;

    payment_id?: string;

    status: transaction_status;
}

export class FilterTransactionDto {
    transaction_id?: string;
    payment_id?: string;
    status?: transaction_status;
}