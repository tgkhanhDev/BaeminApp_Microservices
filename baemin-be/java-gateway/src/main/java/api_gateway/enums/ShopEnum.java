package api_gateway.enums;

import lombok.Getter;

public class ShopEnum {
    @Getter
    public enum ShopLabel {
        FOOD("Food"),
        DRINK("Drink"),
        VEGE("Vege"),
        DESSERT("Dessert"),
        NOODLES("Noodles");

        private final String label;

        ShopLabel(String label) {
            this.label = label;
        }
    }

    @Getter
    public enum ShopLocation {
        Ho_Chi_Minh("Ho_Chi_Minh"),
        Ha_Noi("Ha_Noi"),
        Da_Nang("Da_Nang");

        private final String location;

        ShopLocation(String location) {
            this.location = location;
        }
    }


}
