package br.com.store.configuration.handler;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import br.com.store.exception.RetryRabbitMessageException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryRabbitExceptionHandler extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbit.max-attempts}")
    private Integer maxAttempts;

    @Override
    public boolean isFatal(Throwable t) {
        if (hasRetryException(t)) {
            RetryRabbitMessageException retryRabbitMessageException = (RetryRabbitMessageException) t.getCause();

            Long numberOfRetries=retryRabbitMessageException.getNumberOfRetries();

            if(numberOfRetries>=maxAttempts){
                log.info("method=isFatal send to dlq message={}", retryRabbitMessageException.getReceiveMessage());
                rabbitTemplate.convertAndSend(retryRabbitMessageException.getDlqQueue(), retryRabbitMessageException.getReceiveMessage());
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean hasRetryException(Throwable t) {
        return t.getCause() instanceof RetryRabbitMessageException;
    }
}
