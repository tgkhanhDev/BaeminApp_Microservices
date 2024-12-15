package api_gateway.controller;

import api_gateway.dto.request.CreateCartItem;
import api_gateway.dto.request.ShopFilterRequest;
import api_gateway.dto.request.UpdateCartItem;
import api_gateway.dto.response.FoodResponse;
import api_gateway.dto.response.ShopDetailResponse;
import api_gateway.exception.AuthenException;
import api_gateway.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart-api")
public class CartController {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.cart.queue.name}")
    private String cartQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CartController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get cart items by user id")
    Object getCartByAccountId(@PathVariable("userId") String userId){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "cart.get-all-by-user-id",
                userId,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        var responseMessage = rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }

        return responseMessage;
    }

    @PostMapping("")
    @Operation(summary = "Add item to cart")
    Object addCartItem(@RequestBody(required = true) CreateCartItem cartItem){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Serialize payload
        String payload = "";
        if (cartItem != null) {
            payload = cartItem.toString(); // Convert object to JSON
        }

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "cart.add-item",
                payload,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        var responseMessage = rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }

        return responseMessage;
    }

    @DeleteMapping("/delete-item/{cartId}")
    @Operation(summary = "Delete items from cart")
    Object deleteCartItem(@PathVariable("cartId") String cartId){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "cart.delete-item",
                cartId,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        var responseMessage = rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }

        return responseMessage;
    }

    @DeleteMapping("/empty-cart/{account_id}")
    @Operation(summary = "Empty cart by user id")
    Object emptyCart(@PathVariable("account_id") String account_id){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());
        System.out.println("req: "+ account_id);
        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "cart.empty-cart",
                account_id,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        var responseMessage = rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }
        return responseMessage;
    }

    @PatchMapping("/update-cart-item")
    @Operation(summary = "Add item to cart")
    Object updateCartItem(@RequestBody(required = true) List<UpdateCartItem> cartItem){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Serialize payload
        String payload = "";
        if (cartItem != null) {
            payload = cartItem.toString(); // Convert object to JSON
        }

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "cart.update-cart-item",
                payload,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        var responseMessage = rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }

        return responseMessage;
    }

}
