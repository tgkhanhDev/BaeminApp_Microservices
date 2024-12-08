package api_gateway.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopFilterRequest {
    String label;
    String location;
    String name;


//    {"label":"Food", "location":"Ha_Noi", "name":"Dessert"}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean isFirst = true;
        if (label != null) {
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"label\":\"").append(label).append("\"");
            isFirst = false;
        }
        if (location != null) {
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"location\":\"").append(location).append("\"");
            isFirst = false;
        }
        if (name != null){
            if(!isFirst){
                sb.append(", ");
            }
            sb.append("\"name\":\"").append(name).append("\"");
            isFirst = false;
        }
        sb.append("}");
        return sb.toString();
    }

    public String toJson(ShopFilterRequest filter) {
        try {
            return new ObjectMapper().writeValueAsString(filter);
        } catch (Exception e) {
            throw new RuntimeException("Error converting ShopFilterRequest to JSON", e);
        }
    }
}
