package api_gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;

//@Schema(description = "Details of a shop including its attributes and available foods.")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopDetailResponse {
//        @Schema(description = "Unique identifier for the shop", example = "e58f1b28-470e-485a-bc56-50108d08e197")
        private String shop_id;

//        @Schema(description = "Name of the shop", example = "Bubble Tea Cafe")
        private String shop_name;

//        @Schema(description = "Address of the shop", example = "123 Main Street", nullable = true)
        private String shop_address;

//        @Schema(description = "Thumbnail image URL for the shop", example = "https://example.com/image.jpg", nullable = true)
        private String shop_thumbnail;

//        @Schema(description = "Category of the shop", example = "cafe")
        private String category;

//        @Schema(description = "Label assigned to the shop", example = "Top Rated")
        private String label;

//        @Schema(description = "Location of the shop", example = "Downtown")
        private String location;

//        @Schema(description = "Shop rating", example = "4.5", nullable = true)
        private Double rating;

//        @Schema(description = "Opening time of the shop", example = "09:00:00", nullable = true)
        private String open_time;

//        @Schema(description = "Closing time of the shop", example = "21:00:00", nullable = true)
        private String close_time;

//        @Schema(description = "Starting price range", example = "10000", nullable = true)
        private Integer price_start;

//        @Schema(description = "Ending price range", example = "50000", nullable = true)
        private Integer price_end;

//        @Schema(description = "Indicates whether the shop is open", example = "true")
        private Boolean is_open;

//        @Schema(description = "List of Foods available in the Shop")
        private List<FoodResponse> food;
}
