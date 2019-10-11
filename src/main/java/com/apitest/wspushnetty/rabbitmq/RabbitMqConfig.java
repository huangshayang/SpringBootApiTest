package com.apitest.wspushnetty.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.DEFAULT_CONCURRENT}")
    private int concurrent;

    @Bean
    public Queue QueueS6() {
        return new Queue("S6");
    }

    @Bean
    public Queue QueueS7() {
        return new Queue("S7");
    }

    @Bean(name = "customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(concurrent);  //设置线程数
        factory.setMaxConcurrentConsumers(concurrent); //最大线程数
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
