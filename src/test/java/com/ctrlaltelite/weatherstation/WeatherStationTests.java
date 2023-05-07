package com.ctrlaltelite.weatherstation;

import com.ctrlaltelite.weatherstation.data.entity.WeatherEntry;
import com.ctrlaltelite.weatherstation.data.service.WeatherEntryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WeatherStationTests {

    @Test
    void contextLoads(@Autowired WeatherEntryService weatherEntryService) {
        assertEquals(2, weatherEntryService.count());
        weatherEntryService.update(new WeatherEntry(5, LocalDateTime.of(2023, 5, 6, 8, 30), "New York"));
        assertEquals(3, weatherEntryService.count());
        assertEquals("[WeatherEntry[temperature=70, time=2023-05-02T21:54:54, location='San Jose'], WeatherEntry[temperature=63, time=2023-05-02T21:54:54, location='San Francisco'], WeatherEntry[temperature=5, time=2023-05-06T08:30, location='New York']]",
                weatherEntryService.list(Pageable.unpaged()).stream().toList().toString());
        System.out.println(weatherEntryService.list(Pageable.unpaged()).stream().toList());
    }
}
