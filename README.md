# Kafka-Stream-Combiner
A simple Kafka Streams application that takes two Kafka streams and combines them into a single output stream

## Setup

1.) Setup confluent on your local machine using the provided docker-compose.yml or follow the official set up at https://docs.confluent.io/platform/current/platform-quickstart.html

2.) Create the topics "Customer", "Balance", and "CustomerBalance" from the confluent control center (localhost:9021 by default)

3.) Go to each of the three topics, click on the "schema" option, and paste the contents on the relevant schema file located in the "schema" folder

## How to use

1.) Run the kafkaDemo

2.) Make a GET request to localhost:8080/generate to automatically generate Customer and Balance events

3.) Observe the resulting Customer, Balance, and CustomerBalance messages in the control center

## Notes

This code makes use of Apache Avro schemas and uses a maven plugin to autogenerate class files based on the schemas in kafkaDemo/src/main/schema.

