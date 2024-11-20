package authen_services.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    String email;
    String phoneNumber;
    String firstName;
    String lastName;
    String password;
}
