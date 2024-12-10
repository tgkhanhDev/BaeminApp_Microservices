package api_gateway.controller;

import api_gateway.dto.request.CreateCartItem;
import api_gateway.dto.request.TransactionCreateRequest;
import api_gateway.dto.request.TransactionFilterRequest;
import api_gateway.exception.AuthenException;
import api_gateway.exception.ErrorCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.transaction.queue.name}")
    private String transactionQueue;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TransactionController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //TODO: CATCH STATUS FOR THIS FILTER THEO ENUM
    @GetMapping("")
    Object findTransactions(@ModelAttribute TransactionFilterRequest filter){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Serialize payload
        String payload = "";
        if (filter != null) {
            payload = filter.toString(); // Convert object to JSON
        }
        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "transaction.get-all-filtered",
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

    @PostMapping("")
    Object createTransactions(@RequestBody(required = true) TransactionCreateRequest req){
        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = rabbitTemplate.execute(channel -> channel.queueDeclare().getQueue());

        // Serialize payload
        String payload = "";
        if (req != null) {
            payload = req.toString(); // Convert object to JSON
        }

        // Send the request
        rabbitTemplate.convertAndSend(
                exchange,
                "transaction.create-request",
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
