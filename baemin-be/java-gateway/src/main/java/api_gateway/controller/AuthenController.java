package api_gateway.controller;

import api_gateway.dto.request.AuthenticationRequest;
import api_gateway.dto.request.CreateUserRequest;
import api_gateway.dto.request.IntrospectRequest;
import api_gateway.dto.response.ApiResponse;
import api_gateway.dto.response.AuthenticationResponse;
import api_gateway.dto.response.IntrospectResponse;
import api_gateway.dto.response.UserResponse;
import api_gateway.exception.AuthenException;
import api_gateway.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/auth-api")
public class AuthenController {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.authen.queue.name}")
    private String resQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AuthenController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest payload) throws JsonProcessingException {

        String correlationId = UUID.randomUUID().toString();
        String replyQueue = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                payload,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueue);
                    message.getMessageProperties().setHeader("endpoint", "authApi-login");
                    return message;
                }
        );

        // Wait for response
        String messageBody = (String) rabbitTemplate.receiveAndConvert(replyQueue, 5000); // Receive as String

        if (messageBody != null) {
            try {
                ApiResponse<AuthenticationResponse> res = new ObjectMapper().readValue(
                        messageBody,
                        new TypeReference<ApiResponse<AuthenticationResponse>>() {
                        }
                );

                // Use the response object
                return ApiResponse.<AuthenticationResponse>builder()
                        .data(res.getData())
                        .code(res.getCode())
                        .message(res.getMessage())
                        .build();

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ApiResponse.<AuthenticationResponse>builder()
                .data(null)
                .message(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .build();

    }


    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest payload) throws JsonProcessingException {

        String correlationId = UUID.randomUUID().toString();
        String replyQueue = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                payload,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueue);
                    message.getMessageProperties().setHeader("endpoint", "authApi-introspect");
                    return message;
                }
        );

        // Wait for response
        String messageBody = (String) rabbitTemplate.receiveAndConvert(replyQueue, 5000); // Receive as String

        if (messageBody != null) {

            try {
                ApiResponse<IntrospectResponse> res = new ObjectMapper().readValue(
                        messageBody,
                        new TypeReference<ApiResponse<IntrospectResponse>>() {
                        }
                );

                // Use the response object
                return ApiResponse.<IntrospectResponse>builder()
                        .data(res.getData())
                        .code(res.getCode())
                        .message(res.getMessage())
                        .build();

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ApiResponse.<IntrospectResponse>builder()
                .data(null)
                .message(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .build();
    }

    //
    @PostMapping("/register")
    ApiResponse<UserResponse> createAccount(@RequestBody @Valid CreateUserRequest payload) {
        String correlationId = UUID.randomUUID().toString();
        String replyQueue = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                payload,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueue);
                    message.getMessageProperties().setHeader("endpoint", "authApi-register");
                    return message;
                }
        );

        // Wait for response
        String messageBody = (String) rabbitTemplate.receiveAndConvert(replyQueue, 5000); // Receive as String

        if (messageBody != null) {
            try {
                ApiResponse<UserResponse> res = new ObjectMapper().readValue(
                        messageBody,
                        new TypeReference<ApiResponse<UserResponse>>() {
                        }
                );

                // Use the response object
                return ApiResponse.<UserResponse>builder()
                        .data(res.getData())
                        .code(res.getCode())
                        .message(res.getMessage())
                        .build();

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ApiResponse.<UserResponse>builder()
                .data(null)
                .message(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .build();
    }

    @GetMapping("/profile/{userId}")
    UserResponse getUserProfile(@PathVariable("userId") String userId) {

        try {
            UUID uuid = UUID.fromString(userId); // This will throw an exception if the format is invalid
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format");
        }

        String correlationId = UUID.randomUUID().toString();
        String replyQueue = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                userId,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueue);
                    message.getMessageProperties().setHeader("endpoint", "authApi-getProfile");
                    return message;
                }
        );

        // Wait for response
        String messageBody = (String) rabbitTemplate.receiveAndConvert(replyQueue, 5000); // Receive as String

        if (messageBody != null) {

        }

        return null;
    }

//    @Patch('/update-profile')
//    UpadateUserProfile(@Body() info: UpdateUserDto) {
//        return this.authApiService.updateUserInfo(info);
//    }

}
