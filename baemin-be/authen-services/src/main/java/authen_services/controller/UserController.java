package authen_services.controller;

import authen_services.dto.request.CreateUserRequest;
import authen_services.dto.request.LoginRequest;
import authen_services.dto.response.ApiResponse;
import authen_services.dto.response.UserResponse;
import authen_services.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("OK")
                .data(userService.getMyInfo())
                .build();
    }

    @PostMapping("/create-account")
    public ApiResponse<UserResponse> createAccount(@RequestBody CreateUserRequest req) {
//        userService.createAccount(req);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .data(userService.createAccount(req))
                .build();
    }
}
