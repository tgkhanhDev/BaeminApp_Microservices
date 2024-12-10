package api_gateway.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    String delivery_address;
    String message;
    String status;
    String account_id;
    ArrayList<TransactionCreateRequest> transactions;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean isFirst = true;

        if (delivery_address != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"delivery_address\":\"").append(delivery_address).append("\"");
            isFirst = false;
        }
        if (message != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"message\":\"").append(message).append("\"");
            isFirst = false;
        }
        if (status != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"status\":\"").append(status).append("\"");
            isFirst = false;
        }
        if (account_id != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"account_id\":\"").append(account_id).append("\"");
            isFirst = false;
        }
        if (transactions.size() > 0) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"transactions\":[");
            AtomicInteger counter = new AtomicInteger(0);
            transactions.forEach(transactionCreateRequest -> {
                sb.append(transactionCreateRequest.toString());
                if (counter.incrementAndGet() < transactions.size()) {
                    sb.append(", ");
                }
            });

            sb.append("]");
            isFirst = false;
        }
        sb.append("}");

        return sb.toString();
    }
}
