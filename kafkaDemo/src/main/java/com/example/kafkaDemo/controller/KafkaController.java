package com.example.kafkaDemo.controller;

import com.example.kafkaDemo.schemas.Balance;
import com.example.kafkaDemo.schemas.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
public class KafkaController {
    @Value(value = "${spring.kafka.topics.customer}")
    private String customerTopic;
    @Value(value = "${spring.kafka.topics.balance}")
    private String balanceTopic;
    @Autowired
    private KafkaTemplate<String, Customer> customerKafkaTemplate;
    @Autowired
    private KafkaTemplate<String, Balance> balanceKafkaTemplate;

    @GetMapping(path="/generate")
    public ResponseEntity<String> generate(){
        for (int i = 0; i <100; i++){
            String key = Integer.toString(i);
            Customer customer = Customer.newBuilder()
                    .setCustomerId(key)
                    .setAccountId(key)
                    .setName(key)
                    .build();
            Balance balance = Balance.newBuilder()
                    .setAccountId(key)
                    .setCustomerId(key)
                    .setBalanceAmount((float) i)
                    .build();
            customerKafkaTemplate.send(customerTopic, key, customer);
            balanceKafkaTemplate.send(balanceTopic, key, balance);

        }
        return ResponseEntity.ok("");
    }


}
