package com.kafka.consumer.listner;

import com.kafka.consumer.dto.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaMessageListener {



    // Plain String listener
    @KafkaListener(topics = "newTopic", groupId = "rishabh-1")
    public void consumeStringEvents(String message) {
        log.info("Consumer received String event: {}", message);
    }

    // POJO listener
    @KafkaListener(
            topics = "customer",
            groupId = "rishabh-3",
            containerFactory = "customerKafkaListenerFactory"
    )
    public void consumeCustomerEvents(Customer customer) {
        log.info("Consumer received Customer event: {}", customer);
    }
}
