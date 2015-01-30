package com.mwronski.hateoas;

import com.mwronski.hateoas.model.Message;
import com.mwronski.hateoas.repositories.Repository;
import com.mwronski.hateoas.repositories.memory.InMemoryMessageRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.mwronski.hateoas.log.Tracer.tracer;

/**
 * Main configuration of the application and entry point at the same time.
 *
 * @author Michal Wronski
 * @date 27-05-2014
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RestApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        tracer(RestApplication.class).info("Starting application: %s", RestApplication.class.getName());
        SpringApplication.run(RestApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestApplication.class);
    }

    @Bean(name = "messageRepository")
    public Repository<Message> messageRepository() {
        return new InMemoryMessageRepository();
    }

}
