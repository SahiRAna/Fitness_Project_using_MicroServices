package com.fitness.AiService.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 *║                           RABBITMQ SETUP FOR AI SERVICE                 ║
 *║                                                                              ║
 *║  WHAT THIS DOES: Creates the "pipes" for ai tracking messages           ║
 *║                                                                              ║
 *║  MESSAGE FLOW:  Other Services ──(JSON)──> Exchange ──(routingKey)──> Queue  ║
 *║                                 │                                    │       ║
 *║                                 └─────── Only if routingKey matches ───────┘ ║
 *╚══════════════════════════════════════════════════════════════════════════════╝
 */
@Configuration
public class RabbitMqConfig {

    // 📍 STEP 1: Read names from application.yaml (externalized config)
    @Value("${spring.rabbitmq.template.default-receive-queue}")  // e.g., "activity.queue"
    private String queueName;                                    // Where messages LAND

    @Value("${spring.rabbitmq.template.exchange}")               // e.g., "fitness.exchange"
    private String exchangeName;                                 // Traffic HUB

    @Value("${spring.rabbitmq.template.routing-key}")            // e.g., "activity.tracking"
    private String routingKey;                                   // Message LABEL

    // 🚂 STEP 2: Create the QUEUE (like a mailbox that stores messages)
    @Bean
    public Queue activityQueue(){
        /*
         * 📬 QUEUE = Message Storage Box
         * - Name: From properties (activity.queue)
         * - durable=true → SURVIVES RabbitMQ restart (doesn't lose messages)
         * - If queue exists, RabbitMQ uses it (no duplicates)
         */
        return new Queue(queueName, true);  // true = durable (persists restarts)
    }

    // 🌐 STEP 3: Create the EXCHANGE (like a post office router)
    @Bean
    public DirectExchange activityExchange(){
        /*
         * 📨 EXCHANGE = Message Router/Switchboard
         * - Name: From properties (fitness.exchange)
         * - Type: DIRECT → Only routes if ROUTING KEY exactly matches
         * - All services send messages HERE first
         */
        return new DirectExchange(exchangeName);
    }

    // 🔗 STEP 4: Connect Queue to Exchange (the "delivery route")
    @Bean
    public Binding activityBinding(Queue activityQueue, DirectExchange activityExchange){
        /*
         * 🔗 BINDING = Delivery Rule
         * RULE: Messages arriving at fitness.exchange with routingKey="activity.tracking"
         *       MUST go to activity.queue
         *
         * Flow: fitness.exchange[activity.tracking] ────> activity.queue
         */
        return BindingBuilder
                .bind(activityQueue)           // Take this queue
                .to(activityExchange)          // Connect to this exchange
                .with(routingKey);             // But only for this routing key
    }

    // 🔄 STEP 5: JSON Translator (so Java objects become JSON messages)
    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        // 1. Create the new converter

        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();

        // 2. Create a TypeMapper
        DefaultJacksonJavaTypeMapper typeMapper = new DefaultJacksonJavaTypeMapper();

        // 3. Set the precedence on the Mapper, not the Converter
        // This tells it: "If the @RabbitListener asks for a specific object,
        // use that instead of the class name in the message header."
        typeMapper.setTypePrecedence(JacksonJavaTypeMapper.TypePrecedence.INFERRED);

        // 4. Attach the mapper to the converter
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }



    // 🏭 STEP 6: The Listener Factory (The Engine)
    // We inject 'JacksonJsonMessageConverter' here so we can attach it manually
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter jsonConverter // <--- 1. INJECT YOUR CONVERTER
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        // 2. ATTACH IT! This connects the "INFERRED" logic to the listener
        factory.setMessageConverter(jsonConverter);

        // This handles the errors and stops the infinite loop
        factory.setErrorHandler(new ConditionalRejectingErrorHandler(new GeminiFatalExceptionStrategy()));

        return factory;
    }

    // Inner class to define what exceptions are "Fatal" (Do not retry)
    public static class GeminiFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
        @Override
        public boolean isFatal(Throwable t) {
            // 1. Check standard fatal errors (JSON conversion, etc.)
            if (super.isFatal(t)) {
                return true;
            }

            // 2. Dig deeper to find the WebClient error
            Throwable cause = t;
            while (cause != null) {
                if (cause instanceof WebClientResponseException) {
                    WebClientResponseException ex = (WebClientResponseException) cause;
                    // If it's a 4xx error (400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found)
                    // We STOP retrying. These errors won't fix themselves.
                    if (ex.getStatusCode().is4xxClientError()) {
                        return true;
                    }
                }
                cause = cause.getCause();
            }
            return false;
        }
    }
}
