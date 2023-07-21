package com.example.kafkaDemo.joiner;

import com.example.kafkaDemo.schemas.Balance;
import com.example.kafkaDemo.schemas.Customer;
import com.example.kafkaDemo.schemas.CustomerBalance;
import org.apache.kafka.streams.kstream.ValueJoiner;

//controls how Customer and Balance are joined together
public class CustomerBalanceJoiner implements ValueJoiner<Customer, Balance, CustomerBalance> {
    @Override
    public CustomerBalance apply(Customer customer, Balance balance) {
        return CustomerBalance.newBuilder()
                .setCustomerId(customer.getCustomerId())
                .setAccountId(balance.getAccountId())
                .setName(customer.getName())
                .setBalanceAmount(balance.getBalanceAmount())
                .build();

    }
}
