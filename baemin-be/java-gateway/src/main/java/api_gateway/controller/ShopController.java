package api_gateway.controller;

import api_gateway.dto.response.FoodResponse;
import api_gateway.exception.AuthenException;
import api_gateway.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/food-api")
public class ShopController {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.food.queue.name}")
    private String foodQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ShopController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/food")
    ArrayList<FoodResponse> getFoods() {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "food.get-all",
                "",
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        // Wait for response
        ArrayList<FoodResponse> responseMessage = (ArrayList<FoodResponse>) rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }

        // Process the response
//        System.out.println("Received response: " + responseMessage);

        return responseMessage;
    }

    @GetMapping("/food/{foodId}")
    FoodResponse findFoodById(@PathVariable("foodId") String foodId){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "food.get-by-id",
                foodId,
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

        return new ObjectMapper().convertValue(responseMessage, FoodResponse.class);

    }

//    @GetMapping("/food/{shopId}")
//    FoodResponse findAllFoodByShopId(@PathVariable("shopId") String shopId){
//        String correlationId = UUID.randomUUID().toString();
//        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());
//
//        // Send the request
//        rabbitTemplate.convertAndSend(
//                exchange,
//                "food.get-all-by-shop-id",
//                shopId,
//                message -> {
//                    message.getMessageProperties().setCorrelationId(correlationId);
//                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
//                    return message;
//                }
//        );
//
//        Object responseMessage = rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds
//
//        if (responseMessage.equals("null")) {
//            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
//        }
//        return new ObjectMapper().convertValue(responseMessage, FoodResponse.class);
//    }

}
