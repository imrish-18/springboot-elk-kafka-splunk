package com.kafka.Controller;

import com.kafka.dto.Customer;
import com.kafka.service.KafkaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {
    @Autowired
    private KafkaPublisher publisher;

    @GetMapping("/publish/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable String message) {
        try {
            for (int i = 0; i < 1; i++) {
                publisher.sendMessageToTopic(message + " : " + i);
            }
            return ResponseEntity.ok("message published successfully ..");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    @PostMapping("/publish")
    public void sendEvents(@RequestBody Customer customer) {
        publisher.sendEventsToTopic(customer);
    }
    @PostMapping("/saveCustomer")
    public ResponseEntity<String> saveAndPublish(@RequestBody Customer customer) {
        publisher.saveAndPublish(customer);
        return ResponseEntity.ok("Customer saved and published to Kafka successfully");
    }
}
