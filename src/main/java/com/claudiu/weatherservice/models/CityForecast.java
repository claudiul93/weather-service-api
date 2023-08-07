package com.claudiu.weatherservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityForecast {
  private String day;
  private String temperature;
  private String wind;

  public double getTemperatureValue(){
    var temperatureValue = temperature.substring(0, temperature.indexOf(" "));

    return temperatureValue.isEmpty() ? 0 : Double.parseDouble(temperatureValue);
  }

  public double getWindValue(){
    var windValue = wind.substring(0, wind.indexOf(" "));

    return windValue.isEmpty() ? 0 : Double.parseDouble(windValue);
  }
}
