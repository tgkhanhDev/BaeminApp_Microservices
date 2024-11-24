package authen_services.dto.response;

import authen_services.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    UUID userId;
    String email;
    String phoneNumber;
    String firstName;
    String lastName;
    UserRole role;
    boolean isActive;
}
