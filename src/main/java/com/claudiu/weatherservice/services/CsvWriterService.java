package com.claudiu.weatherservice.services;

import com.claudiu.weatherservice.models.CityWeatherData;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CsvWriterService {

  public void writeWeatherDataToCsv(List<CityWeatherData> cityWeatherDataList) {
    String csvFilePath = "weather.csv";

    try (FileWriter writer = new FileWriter(csvFilePath)) {
      writer.write("Name, temperature, wind\n");
      for (CityWeatherData data : cityWeatherDataList) {
        writer.write(data.getCity() + "," + data.getTemperature() + "," + data.getWind() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
