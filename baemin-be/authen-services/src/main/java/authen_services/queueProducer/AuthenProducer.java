package authen_services.queueProducer;

import authen_services.config.CustomMessageSender;
import authen_services.dto.request.AuthenticationRequest;
import authen_services.dto.request.CreateUserRequest;
import authen_services.dto.request.IntrospectRequest;
import authen_services.dto.request.LoginRequest;
import authen_services.dto.response.ApiResponse;
import authen_services.dto.response.AuthenticationResponse;
import authen_services.dto.response.IntrospectResponse;
import authen_services.dto.response.UserResponse;
import authen_services.exception.AuthenException;
import authen_services.service.AuthenticationService;
import authen_services.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenProducer {
    AuthenticationService authenticationService;
    UserService userService;
    RabbitTemplate rabbitTemplate;

    CustomMessageSender customMessageSender;

    @Autowired
    public AuthenProducer(AuthenticationService authenticationService, UserService userService, RabbitTemplate rabbitTemplate, CustomMessageSender customMessageSender) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
        this.customMessageSender = customMessageSender;
    }

    @RabbitListener(queues = "${rabbitmq.authen.queue.name}")
    public void AuthenQueueListener(Object payload, Message message) throws Exception {
        String header = (String) message.getMessageProperties().getHeader("endpoint");
//        authenticationService.authenticate((AuthenticationRequest) payload, message);
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyToQueue = message.getMessageProperties().getReplyTo();

        if (replyToQueue == null) {
            String res = new ObjectMapper().writeValueAsString
                    (ApiResponse.builder()
                            .data(null)
                            .message("QUEUE NULL")
                            .code(1500)
                            .build());

            rabbitTemplate.convertAndSend(
                    "",
                    replyToQueue,
                    res,
                    m -> {
                        m.getMessageProperties().setCorrelationId(correlationId);
                        return m;
                    }
            );
        }

        String body = new String(message.getBody(), "UTF-8");
        // Use ObjectMapper to convert JSON to AuthenticationRequest
        ObjectMapper objectMapper = new ObjectMapper();

//        System.out.println("header: "+ header + "  body: " + body);

        try {
            switch (header) {
                case "authApi-login":
//                    AuthenticationRequest loginRequest = objectMapper.readValue(body, AuthenticationRequest.class);
//                    AuthenticationResponse loginResponse = authenticationService.authenticate(loginRequest);
                    LoginRequest loginRequest = objectMapper.readValue(body, LoginRequest.class);
                    UserResponse loginResponse = userService.login(loginRequest);
                    customMessageSender.sendResponseDataToProducer(correlationId, replyToQueue, loginResponse);
                    break;
                case "authApi-introspect":
                    IntrospectRequest introspectRequest = objectMapper.readValue(body, IntrospectRequest.class);
                    IntrospectResponse introspectResponse = authenticationService.introspect(introspectRequest);
                    customMessageSender.sendResponseDataToProducer(correlationId, replyToQueue, introspectResponse);
                    break;
                case "authApi-register":
                    CreateUserRequest createUserRequest = objectMapper.readValue(body, CreateUserRequest.class);
                    UserResponse userResponse = userService.createAccount(createUserRequest);
                    customMessageSender.sendResponseDataToProducer(correlationId, replyToQueue, userResponse);
                    break;
                case "authApi-getProfile":
                    UserResponse info = userService.getUserInfo(body.replaceAll("^\"|\"$", ""));
                    customMessageSender.sendResponseDataToProducer(correlationId, replyToQueue, info);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown header: " + header);
            }
        } catch (AuthenException e) {
            // Handle AuthenException and send error response back to producer
            customMessageSender.sendErrorMessageToProducer(correlationId, replyToQueue, e.getMessage(), e.getErrorCode().getCode());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            customMessageSender.sendErrorMessageToProducer(correlationId, replyToQueue, e.getMessage(), 500);
        }
    }

}
