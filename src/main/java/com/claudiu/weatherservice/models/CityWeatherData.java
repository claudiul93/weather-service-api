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

  public static String capitalizeCityName(String city){
    String delimiter = city.contains("-") ? "-" : " ";
    String[] parts = city.split(delimiter);
    StringBuilder result = new StringBuilder();

    for (String part : parts) {
      if (!part.isEmpty()) {
        char firstChar = Character.toUpperCase(part.charAt(0));
        String rest = part.substring(1).toLowerCase();
        result.append(firstChar).append(rest).append(delimiter);
      }
    }

    if (result.length() > 0) {
      result.setLength(result.length() - 1);
    }

    return result.toString();
  }
}
