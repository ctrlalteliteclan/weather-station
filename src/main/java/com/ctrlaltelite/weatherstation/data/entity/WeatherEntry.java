package com.ctrlaltelite.weatherstation.data.entity;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class WeatherEntry extends AbstractEntity {

    private Integer temperature;
    private LocalDateTime time;
    private String location;

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

}
