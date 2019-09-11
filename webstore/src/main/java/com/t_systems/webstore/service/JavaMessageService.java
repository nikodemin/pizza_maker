package com.t_systems.webstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Configuration
@RequiredArgsConstructor
@Log4j
public class JavaMessageService {
    private final ProductService productService;

    @PostConstruct
    private void init()
    {
        productService.setJavaMessageService(this);
    }

    private void sendMessage(String text){
        jmsTemplate().convertAndSend("webstore_top_products",text);
    }


    public void sendTopProducts(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            sendMessage(mapper.writeValueAsString(productService.getTopProductsDto()));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
        }
    }


    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        return connectionFactory;
    }


    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
}