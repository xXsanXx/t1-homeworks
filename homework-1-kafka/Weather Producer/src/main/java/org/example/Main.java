package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (WeatherProducer producer = new WeatherProducer()) {
            producer.startSendingWeatherInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}