package authen_services.service;

import authen_services.dto.request.CreateUserRequest;
import authen_services.dto.request.LoginRequest;
import authen_services.dto.request.UpdateUserRequest;
import authen_services.dto.response.UserResponse;
import authen_services.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse login(LoginRequest req) {
        return UserResponse.builder()
                .userId("LOLOLOL")
                .build();
    }

    @Override
    public UserResponse updateAccount(String userId, UpdateUserRequest req) {
        return null;
    }

    @Override
    public UserResponse createAccount(CreateUserRequest req) {
        return null;
    }

    @Override
    public UserResponse getMyInfo() {
        return UserResponse.builder()
                .email("LOLOLOL")
                .build();
    }
}
