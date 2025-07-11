package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (WeatherConsumer consumer = new WeatherConsumer()) {
            consumer.startGettingWeatherInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}