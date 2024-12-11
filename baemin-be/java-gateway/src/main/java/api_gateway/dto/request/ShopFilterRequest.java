package api_gateway.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopFilterRequest {
    @Nullable
    String label;
    @Nullable
    String location;
    @Nullable
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
