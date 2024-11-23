package authen_services.service;

import authen_services.dto.request.CreateUserRequest;
import authen_services.dto.request.LoginRequest;
import authen_services.dto.request.UpdateUserRequest;
import authen_services.dto.response.UserResponse;
import authen_services.enums.UserRole;
import authen_services.mapper.UserMapper;
import authen_services.model.User;
import authen_services.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponse login(LoginRequest req) {

        UserResponse user = userMapper.toUserResponse(userRepository.findByEmail(req.getEmail()));

        log.info("User: {}", user);

        if (user == null) {
            return null;
        }


        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserResponse updateAccount(String userId, UpdateUserRequest req) {
        return null;
    }

    @Override
    public UserResponse createAccount(CreateUserRequest req) {
        if(userRepository.findByEmail(req.getEmail()) != null) {
            log.info("User {} already exists", req.getEmail());
            return null;
        }

        var pas = passwordEncoder.encode(req.getPassword());

        log.info("Password: {}", pas);
        req.setPassword(pas);

        User user = userMapper.toUser(req);
        user.setRole(UserRole.BUYER);
        user.setActive(true);

//        log.info("Decode: {}", passwordEncoder.matches(req.getPassword(), pas));

        UserResponse userRes = userMapper.toUserResponse(user);
        return userRes;
    }

    @Override
    public UserResponse getMyInfo() {
        return UserResponse.builder()
                .email("LOLOLOL")
                .build();
    }
}
