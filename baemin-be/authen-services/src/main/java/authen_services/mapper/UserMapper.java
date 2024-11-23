package authen_services.mapper;

import authen_services.dto.request.CreateUserRequest;
import authen_services.dto.response.UserResponse;
import authen_services.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);
    UserResponse toUserResponse(User user);
}
