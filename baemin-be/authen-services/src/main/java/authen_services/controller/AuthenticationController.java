package authen_services.controller;

import authen_services.dto.request.AuthenticationRequest;
import authen_services.dto.request.IntrospectRequest;
import authen_services.dto.response.ApiResponse;
import authen_services.dto.response.AuthenticationResponse;
import authen_services.dto.response.IntrospectResponse;
import authen_services.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .data(authenticationService.introspect(request))
                .build();
    }

}

