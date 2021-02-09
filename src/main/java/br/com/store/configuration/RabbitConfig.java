package br.com.store.configuration;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitConfig {

    @Value("${ampq.rabbitmq.ms.fornecedor.event}")
    private String queueFornecedor;

    @Value("${ampq.rabbitmq.exchenge}")
    private String directEx;

    @Bean
    Queue queue() {
        return QueueBuilder.durable(queueFornecedor).build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(directEx);
    }

    @Bean(name = "storeConnectFactory")
    @Primary
    public ConnectionFactory storeConnectFactory(StoreAmqpProperties store) {
        CachingConnectionFactory connect = new CachingConnectionFactory();
        connect.setUri(store.getAddresses());
        connect.setUsername(store.getUsername());
        connect.setPassword(store.getPassword());
        return connect;
    }

    @Bean(name = "storeRabbitTemplate")
    @Primary
    public RabbitTemplate storeRabbitTemplate(@Qualifier("storeConnectFactory") ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        rabbitTemplate.setEncoding("UTF-8");
        return rabbitTemplate;
    }

    @Bean
    @Primary
    public RabbitAdmin storeRabbitAdmin(@Qualifier("storeConnectFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareQueue(queue());
        rabbitAdmin.declareExchange(exchange());
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue()).to(exchange()).with(queueFornecedor));
        return rabbitAdmin;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
