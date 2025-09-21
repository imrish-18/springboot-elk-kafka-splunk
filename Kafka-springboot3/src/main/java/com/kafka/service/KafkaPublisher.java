package com.kafka.service;


import com.kafka.dto.Customer;
import com.kafka.model.CustomerEntity;
import com.kafka.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPublisher.class);
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    public void sendMessageToTopic(String message){
        CompletableFuture<SendResult<String, Object>> future =kafkaTemplate.send("newTopic",message);
        future.whenComplete((result,ex)->{
            if (ex == null) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            } else {
                log.info("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });

    }
    public void sendEventsToTopic(Customer customer) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("customer", customer);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("  Sent message=[{}] with offset=[{}]", customer.toString(), result.getRecordMetadata().offset());
                } else {
                    log.info(" Unable to send message=[{}] due to : {}", customer.toString(), ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("ERROR : "+ ex.getMessage());
        }
    }
    public void saveAndPublish(Customer customer) {
        // 1. Save to DB
        // 1. Map DTO → Entity
        CustomerEntity entity = new CustomerEntity(
                customer.id(),
                customer.name(),
                customer.email(),
                customer.contactNo()
        );

        // 2. Save to DB
        customerRepository.save(entity);

        // 2. Publish to Kafka
        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send("customer", String.valueOf(customer.id()), customer);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info(" Sent message=[{}] with offset=[{}]", customer, result.getRecordMetadata().offset());
                } else {
                    log.info("❌ Unable to send message=[{}] due to : {}", customer, ex.getMessage());
                }
            });
        } catch (Exception ex) {
            System.out.println("🔥 ERROR while sending: " + ex.getMessage());
        }
    }
}
