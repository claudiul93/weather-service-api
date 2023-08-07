package com.claudiu.weatherservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CityWeatherData {
  private String city;
  // Average temperature
  private String temperature;
  // Average wind speed
  private String wind;
}
