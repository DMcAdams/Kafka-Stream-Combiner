package com.example.kafkaDemo.processor;

import com.example.kafkaDemo.joiner.CustomerBalanceJoiner;
import com.example.kafkaDemo.schemas.Balance;
import com.example.kafkaDemo.schemas.Customer;
import com.example.kafkaDemo.schemas.CustomerBalance;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import jakarta.annotation.PostConstruct;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerBalanceProccessor {
    @Value(value = "${spring.kafka.topics.customer}")
    private String customerTopic;
    @Value(value = "${spring.kafka.topics.balance}")
    private String balanceTopic;
    @Value(value = "${spring.kafka.topics.customerBalance}")
    private String customerBalanceTopic;

    @Autowired
    private StreamsBuilder streamsBuilder;
    @Autowired
    private KafkaProperties kafkaProperties;

    @PostConstruct
    public void joinCustomerAndBalance(){
        //create customer and balance streams
        KStream<String, Customer> customerStream = streamsBuilder.stream(customerTopic, Consumed.with(Serdes.String(), this.getSpecificAvroSerde(new SpecificAvroSerde<>())));
        customerStream.peek( (key, customer) -> System.out.println("key:" + key + "value" + customer.toString()));
        KStream<String, Balance> balanceStream = streamsBuilder.stream(balanceTopic, Consumed.with(Serdes.String(), this.getSpecificAvroSerde(new SpecificAvroSerde<>())));
        balanceStream.peek( (key,balance) -> System.out.println("key:" + key + "value" + balance.toString()));
        //modify customer stream to use accountId as key
        KStream<String, Customer> customerStreamRekeyed = customerStream.selectKey((key, value) -> value.get("accountId").toString());
        //join the two streams
        KStream<String, CustomerBalance> customerBalanceStream = customerStreamRekeyed.join(balanceStream, new CustomerBalanceJoiner(), JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(10)), StreamJoined.with(Serdes.String(), this.getSpecificAvroSerde(new SpecificAvroSerde<>()), this.getSpecificAvroSerde(new SpecificAvroSerde<>())));
        //push combined stream to kafka topic
        customerBalanceStream.to(customerBalanceTopic, Produced.with(Serdes.String(), this.getSpecificAvroSerde(new SpecificAvroSerde<>())));
        //build topology and start streams
        Topology topology = streamsBuilder.build();
        KafkaStreams streams = new KafkaStreams(topology, new StreamsConfig(kafkaProperties.buildStreamsProperties()));
        streams.start();
    }
    //registers serde with Kafka Registry
    <T extends SpecificRecord > SpecificAvroSerde<T> getSpecificAvroSerde(SpecificAvroSerde<T> serde){
        Map<String, String> map = new HashMap<>();
        map.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, kafkaProperties.getProperties().get("schema.registry.url"));
        serde.configure(map, false);
        return serde;
    }
}
