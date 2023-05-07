package com.ctrlaltelite.weatherstation.data.service;

import com.ctrlaltelite.weatherstation.data.entity.WeatherEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WeatherEntryRepository
        extends
            JpaRepository<WeatherEntry, Long>,
            JpaSpecificationExecutor<WeatherEntry> {

}
