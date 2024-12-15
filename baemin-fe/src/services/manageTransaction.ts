import { apiInstance } from "../constant/apiInstance";
import { Transaction } from "../types/transaction";

const api = apiInstance("http://localhost:8080");

export const manageTransaction = {
  getTransaction: (payment_id: string) =>
    api.get<Transaction[]>(`/transaction-api?payment_id=${payment_id}`),
};
