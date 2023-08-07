package com.claudiu.weatherservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CityForecast {
  private String day;
  private String temperature;
  private String wind;
}
