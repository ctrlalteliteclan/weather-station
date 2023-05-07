package com.ctrlaltelite.weatherstation.data.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
public class WeatherEntry extends AbstractEntity {

    private Integer temperature;
    private LocalDateTime time;
    private String location;

    public WeatherEntry() {
    }

    public WeatherEntry(Integer temperature, LocalDateTime time, String location) {
        this.temperature = temperature;
        this.time = time;
        this.location = location;
    }

    public Integer getTemperature() {
        return temperature;
    }
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "WeatherEntry[", "]")
                .add("temperature=" + temperature)
                .add("time=" + time)
                .add("location='" + location + "'")
                .toString();
    }
}
