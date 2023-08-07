package com.claudiu.weatherservice.services;

import static org.junit.jupiter.api.Assertions.*;

import com.claudiu.weatherservice.exceptions.CityNotFoundException;
import com.claudiu.weatherservice.models.CityForecast;
import com.claudiu.weatherservice.models.CityWeatherData;
import com.claudiu.weatherservice.models.WeatherData;
import java.util.List;
import org.junit.jupiter.api.Test;
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
    List<String> cities = List.of("Arad", "Bucharest", "Oradea", "ARAd", "Cluj-Napoca", "Bistrita-Nasaud", "Baia Mare");

    // When
    Flux<CityWeatherData> weatherDataFlux = weatherService.getWeatherDataForCities(cities);

    // Then
    StepVerifier.create(weatherDataFlux)
        .expectNextCount(cities.size() - 1)
        .verifyComplete();
  }

  @Test
  void calculateAverages_should_calculate_average_temperature_and_wind() {
    // Given
    String city = "Test-City";
    WeatherData weatherData = new WeatherData();
    List<CityForecast> forecasts = List.of(
        new CityForecast("1", "+25 °C", "10 km/h"),
        new CityForecast("2", "+27 °C", "12 km/h")
    );
    weatherData.setForecast(forecasts);

    // When
    CityWeatherData result = weatherService.calculateAverages(city, weatherData);

    // Then
    assertEquals(city, result.getCity());
    assertEquals("26.0", result.getTemperature());
    assertEquals("11.0", result.getWind());
  }

  @Test
  void getWeatherData_should_throw_exception_when_city_not_found() {
    // Given
    String city = "NonExistentCity";

    // When
    Mono<CityWeatherData> weatherDataMono = weatherService.getWeatherData(city);

    // Then
    StepVerifier.create(weatherDataMono)
        .expectErrorMatches(ex -> ex instanceof CityNotFoundException)
        .verify();
  }

}