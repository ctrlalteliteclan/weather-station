package com.ctrlaltelite.weatherstation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WeatherStationTests {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}