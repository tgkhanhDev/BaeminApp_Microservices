package authen_services.model;

import authen_services.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Table(name = "Users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @UuidGenerator
    @Column(name = "user_id")
    String userId;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "password", nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    UserRole role;

    @Column(name = "is_active", nullable = false)
    boolean isActive;
}
