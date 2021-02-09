package br.com.store.exception;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RetryRabbitMessageException extends Exception {
    /**
    *
    */
    private static final long serialVersionUID = -3620335794136526973L;

    private static final String COUNT = "count";

    private List<Map<String, Object>> xDeath;

    private Object receiveMessage;

    private String dlq;

    public Long getNumberOfRetries() {
        if (xDeath != null && xDeath.size() >= 1) {
            return (Long) xDeath.get(0).get(COUNT) + 1;
        }
        return 0L;
    }

    public String getDlqQueue() {
        return dlq;
    }

}
