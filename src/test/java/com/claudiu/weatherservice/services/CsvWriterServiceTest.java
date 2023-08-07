package com.claudiu.weatherservice.services;

import static org.junit.jupiter.api.Assertions.*;

import com.claudiu.weatherservice.models.CityWeatherData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
class CsvWriterServiceTest {

  @AfterEach
  void cleanup() throws IOException {
      Files.deleteIfExists(Path.of("weather.csv"));
  }

  @Test
  void writeWeatherDataToCsv_should_write_data_to_csv_file() throws IOException {
    // Given
    List<CityWeatherData> cityWeatherDataList = List.of(
        new CityWeatherData("Arad", "25.00", "10.00"),
        new CityWeatherData("Oradea", "30.00", "15.00")
    );

    CsvWriterService csvWriterService = new CsvWriterService();

    // When
    csvWriterService.writeWeatherDataToCsv(cityWeatherDataList);

    // Then
    try (BufferedReader reader = new BufferedReader(new FileReader("weather.csv"))) {
      // Verify the header
      String headerLine = reader.readLine();
      assertEquals("Name, temperature, wind", headerLine);

      // Verify the data
      String line;
      int dataIndex = 0;
      while ((line = reader.readLine()) != null) {
        String[] fields = line.split(",");
        assertEquals(cityWeatherDataList.get(dataIndex).getCity(), fields[0]);
        assertEquals(cityWeatherDataList.get(dataIndex).getTemperature(), fields[1]);
        assertEquals(cityWeatherDataList.get(dataIndex).getWind(), fields[2]);
        dataIndex++;
      }

      // Verify the count
      assertEquals(cityWeatherDataList.size(), dataIndex);
    }
  }

}