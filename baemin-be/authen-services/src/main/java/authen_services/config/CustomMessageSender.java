package authen_services.config;

import authen_services.dto.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendErrorMessageToProducer(String correlationId, String replyToQueue, String message, int code) throws JsonProcessingException {
        String res = new ObjectMapper().writeValueAsString
                (ApiResponse.builder()
                        .data(null)
                        .message(message)
                        .code(code)
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

    public void sendResponseDataToProducer(String correlationId, String replyToQueue, Object data) throws JsonProcessingException {
        String res = new ObjectMapper().writeValueAsString
                (ApiResponse.builder()
                        .data(null)
                        .message("OK")
                        .data(data)
                        .code(200)
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
}
