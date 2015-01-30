package com.mwronski.hateoas;

import com.mwronski.hateoas.model.Message;
import com.mwronski.hateoas.repositories.Repository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * Test context of rest application
 *
 * @author Michal Wronski
 * @date 31-05-2014
 * @see com.mwronski.hateoas.RestApplication
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RestApplicationTest {

    @Bean(name = "messageRepository")
    public Repository<Message> messageRepository() {
        return mock(Repository.class);
    }

}
