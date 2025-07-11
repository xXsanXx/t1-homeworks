package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.*;
import java.util.*;

public class WeatherConsumer implements Closeable {

    private record WeatherMessage(String city, long timestamp, int temperature) {
    }

    private record TemperatureRecord(long timestamp, int temperature) {
    }

    private static class CityWeatherInfo {
        public String city;
        public int maxTemperature = 0;
        public long maxTimestamp = 0L;
        public int minTemperature = 0;
        public long minTimestamp = 0L;
        public final ArrayList<TemperatureRecord> temperatureRecord = new ArrayList<>();

        public CityWeatherInfo(String city) {
            this.city = city;
        }
    }

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "weather";
    private static final String GROUP_ID = "weather-group";

    private final KafkaConsumer<String, String> consumer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HashMap<String, CityWeatherInfo> statistics = new HashMap<>();

    public WeatherConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put("auto.offset.reset", "earliest");

        consumer = new KafkaConsumer<>(properties);
    }

    public void startGettingWeatherInfo() {
        consumer.subscribe(Collections.singletonList(TOPIC));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    WeatherMessage weatherMessage = objectMapper.readValue(record.value(), WeatherMessage.class);

                    if (!statistics.containsKey(weatherMessage.city)) {
                        statistics.put(weatherMessage.city, new CityWeatherInfo(weatherMessage.city));
                    }

                    System.out.printf("Received weather message: %s\n", weatherMessage.toString());

                    CityWeatherInfo weatherInfo = statistics.get(weatherMessage.city);
                    weatherInfo.temperatureRecord.add(
                            new TemperatureRecord(weatherMessage.timestamp, weatherMessage.temperature)
                    );

                    if (weatherMessage.temperature > weatherInfo.maxTemperature) {
                        weatherInfo.maxTemperature = weatherMessage.temperature;
                        weatherInfo.maxTimestamp = weatherMessage.timestamp;
                    }

                    if (weatherMessage.temperature < weatherInfo.minTemperature) {
                        weatherInfo.minTemperature = weatherMessage.temperature;
                        weatherInfo.minTimestamp = weatherMessage.timestamp;
                    }


                    LocalDateTime maxTemperatureTime =
                            Instant.ofEpochMilli(weatherInfo.maxTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime minTemperatureTime =
                            Instant.ofEpochMilli(weatherInfo.minTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    System.out.printf(
                            "Город: %s. Максимальная температура %d С (%s). Минимальная температура %d C (%s)\n",
                            weatherInfo.city,
                            weatherInfo.maxTemperature, maxTemperatureTime.toString(),
                            weatherInfo.minTemperature, minTemperatureTime.toString()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        consumer.close();
    }
}

