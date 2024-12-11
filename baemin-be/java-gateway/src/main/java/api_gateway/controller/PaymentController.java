package api_gateway.controller;

import api_gateway.dto.request.CreatePaymentRequest;
import api_gateway.dto.request.TransactionCreateRequest;
import api_gateway.dto.request.TransactionFilterRequest;
import api_gateway.exception.AuthenException;
import api_gateway.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.payment.queue.name}")
    private String paymentQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PaymentController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get all payments by user id")
    public Object findPaymentsByUserId(@PathVariable("accountId") String accountId) {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        rabbitTemplate.convertAndSend(
                exchange,
                "payment.find-by-user-id",
                accountId,
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
    @Operation(summary = "Paying cart / Buy now")
    public Object createPayment(@RequestBody CreatePaymentRequest request) {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Serialize payload
        String payload = "";
        if (request != null) {
            payload = request.toString(); // Convert object to JSON
        }

//        System.out.println("payloadCreatePayment: " + payload);

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "payment.create-payment",
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

    @PatchMapping("/{paymentId}")
    @Operation(summary = "Paying for payment (set status)")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "This action will also deduct the amount in food stock\n",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = TransactionCreateRequest.class))
//                    })
//            }
//    )
//    This action will also deduct the amount in food stock
    public Object payForPayment(@PathVariable("paymentId") String paymentId) {
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "payment.pay-for-payment",
                paymentId,
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
