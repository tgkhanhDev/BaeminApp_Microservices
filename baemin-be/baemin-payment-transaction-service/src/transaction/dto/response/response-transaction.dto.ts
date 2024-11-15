import { transaction_status } from "@prisma/client";

export class ResponseTransactionDto {
    transaction_id: string;

    food_id: string;

    quantity: number;

    per_price: number;

    payment_id?: string;

    status: transaction_status;

    //Payment
}