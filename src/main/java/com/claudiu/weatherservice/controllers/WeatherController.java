package com.claudiu.weatherservice.controllers;

import com.claudiu.weatherservice.models.CityWeatherData;
import com.claudiu.weatherservice.services.CsvWriterService;
import com.claudiu.weatherservice.services.WeatherService;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather")
@AllArgsConstructor
public class WeatherController {
  private WeatherService weatherService;
  private CsvWriterService csvWriterService;

  @GetMapping
  public Mono<List<CityWeatherData>> getWeather(@RequestParam List<String> city) {
    Flux<CityWeatherData> weatherDataFlux = weatherService.getWeatherDataForCities(city);

    return weatherDataFlux
        .collectSortedList(Comparator.comparing(CityWeatherData::getCity))
        .doOnNext(csvWriterService::writeWeatherDataToCsv);
  }

}
