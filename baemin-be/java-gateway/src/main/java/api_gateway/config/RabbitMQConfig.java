package api_gateway.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    //? DEADLETTER
    @Value("${rabbitmq.dlq.exchange.name}")
    private String dlqExchange;
    @Value("${rabbitmq.error.queue.name}")
    private String deadQueue;
    @Value("${rabbitmq.dlq.routing.key}")
    private String dlqRoutingKey;

    //TODO: Authen
    @Value("${rabbitmq.authen.queue.name}")
    private String resQueue;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    //TODO: Food
    @Value("${rabbitmq.food.queue.name}")
    private String foodQueue;
    @Value("${rabbitmq.food.routing.key}")
    private String foodRoutingKey;

    //TODO: Shop
    @Value("${rabbitmq.shop.queue.name}")
    private String shopQueue;
    @Value("${rabbitmq.shop.routing.key}")
    private String shopRoutingKey;

    //TODO: Cart
    @Value("${rabbitmq.cart.queue.name}")
    private String cartQueue;
    @Value("${rabbitmq.cart.routing.key}")
    private String cartRoutingKey;

    //TODO: Payment
    @Value("${rabbitmq.payment.queue.name}")
    private String paymentQueue;
    @Value("${rabbitmq.payment.routing.key}")
    private String paymentRoutingKey;

    //TODO: Transaction
    @Value("${rabbitmq.transaction.queue.name}")
    private String transactionQueue;
    @Value("${rabbitmq.transaction.routing.key}")
    private String transactionRoutingKey;

    //!spring bean for rabbitmq queue
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(resQueue)
                .withArgument("x-dead-letter-exchange", dlqExchange) // Dead Letter Exchange
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey) // Dead Letter Routing Key
                .build();
    }

    @Bean
    public Queue foodQueue() {
        return QueueBuilder.durable(foodQueue)
                .withArgument("x-dead-letter-exchange", dlqExchange) // Dead Letter Exchange
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey) // Dead Letter Routing Key
                .build();
    }

    @Bean
    public Queue shopQueue() {
        return QueueBuilder.durable(shopQueue)
                .withArgument("x-dead-letter-exchange", dlqExchange) // Dead Letter Exchange
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey) // Dead Letter Routing Key
                .build();
    }

    @Bean
    public Queue cartQueue() {
        return QueueBuilder.durable(cartQueue)
                .withArgument("x-dead-letter-exchange", dlqExchange) // Dead Letter Exchange
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey) // Dead Letter Routing Key
                .build();
    }

    @Bean
    public Queue paymentQueue() {
        return QueueBuilder.durable(paymentQueue)
                .withArgument("x-dead-letter-exchange", dlqExchange) // Dead Letter Exchange
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey) // Dead Letter Routing Key
                .build();
    }

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder.durable(transactionQueue)
                .withArgument("x-dead-letter-exchange", dlqExchange) // Dead Letter Exchange
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey) // Dead Letter Routing Key
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(deadQueue, true);
    }

    //!spring bean for rabbitMQ exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(dlqExchange);
    }

    //!binding between queue and exchange using routing key
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Binding foodbinding() {
        return BindingBuilder.bind(foodQueue())
                .to(exchange())
                .with(foodRoutingKey);
    }

    @Bean
    public Binding shopbinding() {
        return BindingBuilder.bind(shopQueue())
                .to(exchange())
                .with(shopRoutingKey);
    }
    @Bean
    public Binding cartbinding() {
        return BindingBuilder.bind(cartQueue())
                .to(exchange())
                .with(cartRoutingKey);
    }
    @Bean
    public Binding paymentbinding() {
        return BindingBuilder.bind(paymentQueue())
                .to(exchange())
                .with(paymentRoutingKey);
    }
    @Bean
    public Binding transactionbinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(exchange())
                .with(transactionRoutingKey);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(dlqExchange())
                .with(dlqRoutingKey);
    }

    //Config for communicating via JSON
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    //ConnectionFactory
    //RabbitTemplate
    //RabbitAdmin
}
