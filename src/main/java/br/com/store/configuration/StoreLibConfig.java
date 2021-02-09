package br.com.store.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "br.com.store.library")
@EntityScan("br.com.store.library.domain")
@EnableJpaRepositories("br.com.store.library.repository")
public class StoreLibConfig {
     
}
