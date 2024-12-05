package api_gateway.controller;

import api_gateway.dto.response.ApiResponse;
import api_gateway.dto.response.AuthenticationResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/food-api")
public class FoodController {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.foodshop.routing.key}")
    private String foodRoutingKey;

    @Value("${rabbitmq.foodshop.queue.name}")
    private String foodQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public FoodController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/food")
    ApiResponse<AuthenticationResponse> getFoods() {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Send the request
        rabbitTemplate.convertAndSend(
                "java_gateway_exchange",
                "java_gateway_routing_key",
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        // Wait for response
        String responseMessage = (String) rabbitTemplate.receiveAndConvert(replyQueueName, 10000); // Wait for up to 5 seconds

        if (responseMessage == null) {
            throw new RuntimeException("No response received within the timeout period");
        }

        // Process the response
        System.out.println("Received response: " + responseMessage);

        return null; // Return appropriate response to the client
    }

}
