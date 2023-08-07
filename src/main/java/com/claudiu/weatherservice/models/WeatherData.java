package com.claudiu.weatherservice.models;

import java.util.List;
import lombok.Data;

@Data
public class WeatherData {
  private String city;
  private String temperature;
  private String wind;
  private List<CityForecast> forecasts;
}
