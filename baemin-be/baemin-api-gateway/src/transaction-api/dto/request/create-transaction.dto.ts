
export class CreateTransactionDto {
    food_id: string;

    quantity: number;

    per_price: number;

    payment_id?: string;

    status: string;
}

export class FilterTransactionDto {
    transaction_id?: string;
    payment_id?: string;
    status?: string;
}