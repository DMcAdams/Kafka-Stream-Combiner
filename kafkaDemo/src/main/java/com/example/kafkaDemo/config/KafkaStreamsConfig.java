package com.example.kafkaDemo.config;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

@EnableKafkaStreams
@Configuration
public class KafkaStreamsConfig {
    @Value(value = "${spring.kafka.bootstrap}")
    private String bootstrap;
    @Value(value="${spring.application.name}")
    private String appName;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put("APPLICATION_ID_CONFIG", appName);
        props.put("BOOTSTRAP_SERVERS_CONFIG", bootstrap);
        props.put("DEFAULT_KEY_SERDE_CLASS_CONFIG", Serdes.String().getClass().getName());
        props.put("DEFAULT_VALUE_SERDE_CLASS_CONFIG", Serdes.String().getClass().getName());
        return new KafkaStreamsConfiguration(props);
    }
}
