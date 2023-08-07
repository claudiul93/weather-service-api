package com.claudiu.weatherservice.services;

import static org.junit.jupiter.api.Assertions.*;

import com.claudiu.weatherservice.models.CityForecast;
import com.claudiu.weatherservice.models.CityWeatherData;
import com.claudiu.weatherservice.models.WeatherData;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class WeatherServiceTest {

  WeatherService weatherService = new WeatherService();

  @Test
  void getWeatherData_should_return_weather_data_for_city() {
    // Given
    String city = "Bucharest";
    // When
    Mono<CityWeatherData> weatherDataMono = weatherService.getWeatherData(city);

    // Then
    StepVerifier.create(weatherDataMono)
        .expectNextMatches(data -> data.getCity().equals(city))
        .verifyComplete();
  }

  @Test
  void getWeatherDataForCities_should_return_weather_data_for_multiple_cities() {
    // Given
    List<String> cities = List.of("London", "Bucharest", "Berlin");

    // When
    Flux<CityWeatherData> weatherDataFlux = weatherService.getWeatherDataForCities(cities);

    // Then
    StepVerifier.create(weatherDataFlux)
        .expectNextCount(cities.size())
        .verifyComplete();
  }

  @Test
  void calculateAverages_should_calculate_average_temperature_and_wind() {
    // Given
    WeatherData weatherData = new WeatherData();
    List<CityForecast> forecasts = List.of(
        new CityForecast("Day1", "25", "10"),
        new CityForecast("Day2", "27", "12")
    );
    weatherData.setCity("TestCity");
    weatherData.setForecasts(forecasts);

    // When
    CityWeatherData result = weatherService.calculateAverages(weatherData);

    // Then
    assertEquals("TestCity", result.getCity());
    assertEquals("26.00", result.getTemperature());
    assertEquals("11.00", result.getWind());
  }

  @Test
  void getWeatherData_should_throw_exception_when_city_not_found() {
    // Given
    String city = "NonExistentCity";

    // When
    Mono<CityWeatherData> weatherDataMono = weatherService.getWeatherData(city);

    // Then
    StepVerifier.create(weatherDataMono)
        .expectErrorMatches(ex -> ex instanceof WebClientResponseException.NotFound)
        .verify();
  }

}