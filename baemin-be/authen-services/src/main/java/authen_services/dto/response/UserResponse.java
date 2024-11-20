package authen_services.dto.response;

import authen_services.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String userId;
    String email;
    String phoneNumber;
    String firstName;
    String lastName;
    UserRole role;
    boolean isActive;
}
