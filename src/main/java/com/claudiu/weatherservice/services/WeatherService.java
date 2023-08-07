package com.claudiu.weatherservice.services;

import com.claudiu.weatherservice.models.CityForecast;
import com.claudiu.weatherservice.models.CityWeatherData;
import com.claudiu.weatherservice.models.WeatherData;
import java.util.List;
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
        .map(this::calculateAverages);
  }

  public Flux<CityWeatherData> getWeatherDataForCities(List<String> cities) {
    return Flux.fromIterable(cities)
        .flatMap(this::getWeatherData);
  }

  CityWeatherData calculateAverages(WeatherData weatherData){
    double totalTemperature = 0.0;
    double totalWind = 0.0;

    for (CityForecast forecast : weatherData.getForecasts()) {
      totalTemperature += Double.parseDouble(forecast.getTemperature());
      totalWind += Double.parseDouble(forecast.getWind());
    }

    int size = weatherData.getForecasts().size();
    var averageTemperature = String.format("%.2f", totalTemperature / size);
    var averageWind = String.format("%.2f", totalWind / size);

    return new CityWeatherData(weatherData.getCity(), averageTemperature, averageWind);
  }
}
