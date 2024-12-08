package api_gateway.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartItem {

    String account_id;

    String food_id;

    int quantity;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean isFirst = true;
        if (account_id != null) {
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"account_id\":\"").append(account_id).append("\"");
            isFirst = false;
        }
        if (food_id != null) {
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"food_id\":\"").append(food_id).append("\"");
            isFirst = false;
        }
        if (quantity != 0){
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"quantity\":").append(quantity);
            isFirst = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
