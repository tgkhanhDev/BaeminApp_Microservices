package api_gateway.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionFilterRequest {
    String transaction_id;
    String payment_id;
    String status;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean isFirst = true;
        if (transaction_id != null) {
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"transaction_id\":\"").append(transaction_id).append("\"");
            isFirst = false;
        }
        if (payment_id != null) {
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"payment_id\":\"").append(payment_id).append("\"");
            isFirst = false;
        }
        if (status != null){
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"status\":").append(status);
            isFirst = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
