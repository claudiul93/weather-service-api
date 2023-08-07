package com.claudiu.weatherservice.models;

import java.util.List;
import lombok.Data;

@Data
public class WeatherData {
  private String temperature;
  private String wind;
  private String description;
  private List<CityForecast> forecast;
}
