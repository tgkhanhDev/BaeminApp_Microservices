package authen_services.service;

import authen_services.dto.request.CreateUserRequest;
import authen_services.dto.request.LoginRequest;
import authen_services.dto.request.UpdateUserRequest;
import authen_services.dto.response.UserResponse;
import authen_services.enums.UserRole;
import authen_services.exception.AuthenException;
import authen_services.exception.ErrorCode;
import authen_services.mapper.UserMapper;
import authen_services.model.User;
import authen_services.repository.UserRepository;
import jakarta.persistence.EnumType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
//        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponse login(LoginRequest req) {

        var user = userMapper.toUserResponse(userRepository.findByEmail(req.getEmail()).orElse(null));

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
    @Transactional
    public UserResponse updateAccount(String userId, UpdateUserRequest req) {
        return null;
    }

    //TODO---   Return: ApiResponse<UserResponse>
    @Override
    @Transactional
    public UserResponse createAccount(CreateUserRequest req) {
        // Kiểm tra user đã tồn tại
        var userFound = userRepository.findByEmail(req.getEmail());

        if (userFound.isPresent()) {
            throw new AuthenException(ErrorCode.USER_EXISTED);
        }

        userFound = userRepository.findByPhoneNumber(req.getPhoneNumber());
        if(userFound.isPresent()){
            throw new AuthenException(ErrorCode.PHONE_EXISTED);
        }

        // Mã hóa password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String hashedPassword = passwordEncoder.encode(req.getPassword());
        req.setPassword(hashedPassword);

        // Ánh xạ DTO thành Entity
        User user = userMapper.toUser(req);
        user.setRole(UserRole.valueOf("BUYER")); // Đảm bảo enum chính xác
        user.setActive(true);

        // Lưu vào database
        User savedUser = userRepository.save(user);

        // Trả về DTO phản hồi
        return userMapper.toUserResponse(savedUser);
    }


    @Override
    public UserResponse getMyInfo() {
        return UserResponse.builder()
                .email("LOLOLOL")
                .build();
    }
}
