package api_gateway.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionCreateRequest {
    String food_id;
    String food_name;
    int per_price;
    String type;
    String food_thumbnail;
    int quantity;
    String payment_id;
    String shop_id;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean isFirst = true;

        if (food_id != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"food_id\":\"").append(food_id).append("\"");
            isFirst = false;
        }
        if (food_name != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"food_name\":\"").append(food_name).append("\"");
            isFirst = false;
        }
        if (per_price > 0) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"per_price\":").append(per_price);
            isFirst = false;
        }
        if (type != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"type\":\"").append(type).append("\"");
            isFirst = false;
        }
        if (food_thumbnail != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"food_thumbnail\":\"").append(food_thumbnail).append("\"");
            isFirst = false;
        }
        if (quantity > 0) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"quantity\":").append(quantity);
            isFirst = false;
        }
        if (payment_id != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"payment_id\":\"").append(payment_id).append("\"");
            isFirst = false;
        }
        if (shop_id != null) {
            if (!isFirst) {
                sb.append(", ");
            }
            sb.append("\"shop_id\":\"").append(shop_id).append("\"");
            isFirst = false;
        }

        sb.append("}");
        return sb.toString();
    }
}
