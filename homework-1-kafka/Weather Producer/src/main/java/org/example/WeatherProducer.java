package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.util.Properties;
import java.util.Random;

public class WeatherProducer implements Closeable {

    private record WeatherMessage(String city, long timestamp, int temperature) {
    }

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "weather";

    private static final String[] CITIES = {
            "Санкт-Петербург",
            "Москва",
            "Сочи",
            "Петрозаводск"
    };

    private final KafkaProducer<String, String> producer;

    private final Random random = new Random();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(properties);
    }

    public void startSendingWeatherInfo() {
        try {
            while (true) {
                System.out.println("Sending...");

                for (String city : CITIES) {
                    String message = objectMapper.writeValueAsString(
                            new WeatherMessage(city, Instant.now().toEpochMilli(), random.nextInt(-30, 36))
                    );
                    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, message);

                    producer.send(producerRecord);
                }

                producer.flush();

                System.out.println("Success");

                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void close() throws IOException {
        producer.close();
    }
}
