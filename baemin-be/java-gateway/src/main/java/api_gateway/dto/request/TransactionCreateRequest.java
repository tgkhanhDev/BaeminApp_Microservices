package api_gateway.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionCreateRequest {
    @NotNull(message = "Food ID is required")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Food ID must be a valid UUID")
    String food_id;

    @NotBlank(message = "Food name cannot be empty")
    String food_name;

    @Min(value = 0, message = "Price should be a positive value")
    int per_price;

    @NotNull(message = "Type is required")
    String type;

    @URL(message = "Food thumbnail must be a valid URL")
    String food_thumbnail;

    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is required")
    int quantity;

//    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
//            message = "Food ID must be a valid UUID")
//    String payment_id;

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Food ID must be a valid UUID")
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
//        if (payment_id != null) {
//            if (!isFirst) {
//                sb.append(", ");
//            }
//            sb.append("\"payment_id\":\"").append(payment_id).append("\"");
//            isFirst = false;
//        }
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
