package authen_services.service;

import authen_services.dto.request.CreateUserRequest;
import authen_services.dto.request.LoginRequest;
import authen_services.dto.request.UpdateUserRequest;
import authen_services.dto.response.UserResponse;

public interface UserService {
    public UserResponse login(LoginRequest req);

    public UserResponse updateAccount(String userId, UpdateUserRequest req);

    public UserResponse createAccount(CreateUserRequest req);

    public UserResponse getMyInfo();

}
