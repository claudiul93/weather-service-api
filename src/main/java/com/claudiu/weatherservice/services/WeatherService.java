package com.claudiu.weatherservice.services;

import com.claudiu.weatherservice.exceptions.CityNotFoundException;
import com.claudiu.weatherservice.models.CityForecast;
import com.claudiu.weatherservice.models.CityWeatherData;
import com.claudiu.weatherservice.models.WeatherData;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class WeatherService {
  private static final String URL = "https://goweather.herokuapp.com";

  public Mono<CityWeatherData> getWeatherData(String city) {
    WebClient webClient = WebClient.create(URL);

    return webClient.get()
        .uri("/weather/{city}", city)
        .retrieve()
        .bodyToMono(WeatherData.class)
        .map(weatherData -> {
          if(weatherData.getTemperature().isEmpty() || weatherData.getWind().isEmpty()){
            throw new CityNotFoundException(city);
          }
          return calculateAverages(city, weatherData);
        });
  }

  public Flux<CityWeatherData> getWeatherDataForCities(List<String> cityInput) {
    Set<String> cities = cityInput.stream().map(String::toLowerCase).collect(Collectors.toSet());
    return Flux.fromIterable(cities)
        .flatMap(this::getWeatherData);
  }

  CityWeatherData calculateAverages(String city, WeatherData weatherData){
    double totalTemperature = 0.0;
    double totalWind = 0.0;

    for (CityForecast forecast : weatherData.getForecast()) {
      totalTemperature += forecast.getTemperatureValue();
      totalWind += forecast.getWindValue();
    }

    int size = weatherData.getForecast().size();
    var averageTemperature = String.format("%.1f", totalTemperature / size);
    var averageWind = String.format("%.1f", totalWind / size);

    return new CityWeatherData(CityWeatherData.capitalizeCityName(city), averageTemperature, averageWind);
  }
}
