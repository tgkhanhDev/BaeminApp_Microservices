package authen_services.enums;

import authen_services.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "CartItem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @UuidGenerator
    @Column(name = "cart_item_id")
    String cartItemId;
    @Column(name = "food_id")
    String foodId;
    @Column(name = "quantity")
    int quantity;
//    String accountId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User userId;
}
