package br.com.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.store.library.domain.Buy;
import br.com.store.library.domain.dto.event.RequestEvent;
import br.com.store.library.repository.command.BuyRepositoryCommand;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/teste")
@AllArgsConstructor

public class Teste {
    
    private BuyRepositoryCommand com;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED)
    public void teste(@RequestBody String states){
        RequestEvent request=RequestEvent.builder()
        .cdRequest(1L)
        .timeToPrepare(1)
        .build();
        Buy buy=Buy.of(request, 1L);
        buy.registerNewFornecedor("SP"); 
        com.save(buy);
    }
}
