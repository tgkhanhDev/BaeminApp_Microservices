package api_gateway.controller;

import api_gateway.dto.request.ShopFilterRequest;
import api_gateway.dto.response.FoodResponse;
import api_gateway.dto.response.ShopDetailResponse;
import api_gateway.enums.ShopEnum;
import api_gateway.exception.AuthenException;
import api_gateway.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/shop-api")
public class ShopController {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.shop.queue.name}")
    private String shopQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ShopController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get all shop with filter")
    ArrayList<ShopDetailResponse> getShops(
            @Parameter(description = "Filter shops by label (e.g., FOOD, DRINK, etc.).",
                    schema = @Schema(implementation = ShopEnum.ShopLabel.class), required = false)
            @RequestParam(required = false) ShopEnum.ShopLabel label,

            @Parameter(description = "Filter shops by location (e.g. Ho_Chi_Minh, Ha_Noi, etc.).",
                    schema = @Schema(implementation = ShopEnum.ShopLocation.class), required = false)
            @RequestParam(required = false) ShopEnum.ShopLocation location,

            @RequestParam(required = false) String name
    ) throws JsonProcessingException {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        ShopFilterRequest filter = ShopFilterRequest.builder()
                .label(label != null ? label.getLabel() : "")
                .location(location != null ? location.getLocation(): "")
                .name(name)
                .build();

//        System.out.println("filter: "+ filter);

        // Serialize payload
        String payload = "";
        if (filter != null) {
            payload = filter.toString(); // Convert object to JSON
        }
//        System.out.println("payload: "+ payload);

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "shop.get-all-filtered",
                payload,
                message -> {
                    message.getMessageProperties().setCorrelationId(correlationId);
                    message.getMessageProperties().setReplyTo(replyQueueName); // Dynamic reply queue
                    return message;
                }
        );

        // Wait for response
        ArrayList<ShopDetailResponse> responseMessage = (ArrayList<ShopDetailResponse>) rabbitTemplate.receiveAndConvert(replyQueueName, 5000); // Wait for up to 5 seconds

        if (responseMessage.equals("null")) {
            throw new AuthenException(ErrorCode.USER_NOT_EXISTED);
        }

        // Process the response
//        System.out.println("Received response: " + responseMessage);

        return responseMessage;
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Get shop detail by Id")
    ShopDetailResponse findShopById(@PathVariable("shopId") String shopId){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "shop.get-by-id",
                shopId,
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

        // Process the response

        return new ObjectMapper().convertValue(responseMessage, ShopDetailResponse.class);

    }

}
