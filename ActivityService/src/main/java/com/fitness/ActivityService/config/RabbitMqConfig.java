package com.fitness.ActivityService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 *║                           RABBITMQ SETUP FOR ACTIVITY SERVICE                 ║
 *║                                                                              ║
 *║  WHAT THIS DOES: Creates the "pipes" for activity tracking messages           ║
 *║                                                                              ║
 *║  MESSAGE FLOW:  Other Services ──(JSON)──> Exchange ──(routingKey)──> Queue  ║
 *║                                 │                                    │       ║
 *║                                 └─────── Only if routingKey matches ───────┘ ║
 *╚══════════════════════════════════════════════════════════════════════════════╝
 */
@Configuration
public class RabbitMqConfig {

    // 📍 STEP 1: Read names from application.properties (externalized config)
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
    public MessageConverter jsonMessageConvertor(){
        /*
         * 🔄 MESSAGE CONVERTER = Language Translator
         * - Java Object → JSON (when sending)
         * - JSON → Java Object (when receiving)
         *
         * Without this: rabbitTemplate.convertAndSend(userObject) would fail
         * With this:    rabbitTemplate.convertAndSend(userObject) works perfectly
         */
        return new JacksonJsonMessageConverter();
    }
}
