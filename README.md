# Kafka-Stream-Combiner
A simple Kafka Streams application that takes two Kafka streams and combines them into a single output stream

# Setup
1.) Setup confluent on your local machine using the provided image or follow the official set up at https://docs.confluent.io/platform/current/platform-quickstart.html
2.) Create the topics "Customer", "Balance", and "CustomerBalance" from the control center (localhost:9021 by default)
3.) Go to each of the three topics, click on the "schema" option, and paste the contents on the relevant schema file located in the "schema" folder
