package api_gateway.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItem {

    String cart_item_id;

    int quantity;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean isFirst = true;
        if (cart_item_id != null) {
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"cart_item_id\":\"").append(cart_item_id).append("\"");
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
