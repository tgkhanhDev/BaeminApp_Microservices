package api_gateway.controller;

import api_gateway.dto.response.FoodResponse;
import api_gateway.exception.AuthenException;
import api_gateway.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/cache-api")
public class CacheController {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.food.queue.name}")
    private String foodQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CacheController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get Cache")
    String getCache(@PathVariable("key") String key) {
        {
            String correlationId = UUID.randomUUID().toString();
            String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

            System.out.println("key: " + key);
            // Send the request
            rabbitTemplate.convertAndSend(
                    exchange,
                    "food.get-cache-by-key",
                    key,
                    message -> {
                        message.getMessageProperties().setCorrelationId(correlationId);
                        message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                        return message;
                    }
            );

            // Wait for response
            String responseMessage = (String) rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

            if (responseMessage.equals("null")) {
                throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
            }

            // Process the response
//        System.out.println("Received response: " + responseMessage);

            return responseMessage;
        }
    }

    @PostMapping("")
    @Operation(summary = "Add item to cart")
    String createCache(@RequestBody(required = true) CacheRequest req) throws JsonProcessingException {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Serialize payload
        String payload = "";
        if (req != null) {
            payload = new ObjectMapper().writeValueAsString(req); // Convert object to JSON
        }
        System.out.println("payload: " + payload);
        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "food.add-cache",
                payload,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        Object responseMessage = rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }

//        System.out.println("res: " + responseMessage);
        // Process the response

        return responseMessage.toString();

    }


    @DeleteMapping("/delete/{key}")
    @Operation(summary = "Delete Cache")
    String deleteCache(@PathVariable("key") String key) {
        {
            String correlationId = UUID.randomUUID().toString();
            String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

            // Send the request
            rabbitTemplate.convertAndSend(
                    exchange,
                    "food.delete-cache-by-key",
                    key,
                    message -> {
                        message.getMessageProperties().setCorrelationId(correlationId);
                        message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                        return message;
                    }
            );

            // Wait for response
            String responseMessage = (String) rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

            if (responseMessage.equals("null")) {
                throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
            }

            // Process the response
//        System.out.println("Received response: " + responseMessage);

            return responseMessage;
        }
    }

}

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CacheRequest {
    private String key;
    private Object value;
}