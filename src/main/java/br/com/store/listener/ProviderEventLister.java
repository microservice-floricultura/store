package br.com.store.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import br.com.store.library.domain.dto.event.ProviderRequestEvent;
import br.com.store.producer.MessageProducer;

@Service("ProviderEventLister")
public class ProviderEventLister {
    
    @Autowired
    private MessageProducer producer;

    @Value("${ampq.rabbitmq.ms.fornecedor.event}")
    private String providerRequestEventQueue;

    @Async
    @TransactionalEventListener
    public void onEvent(ProviderRequestEvent providerEvent){
        producer.publish(providerEvent, providerRequestEventQueue);
    }
}
