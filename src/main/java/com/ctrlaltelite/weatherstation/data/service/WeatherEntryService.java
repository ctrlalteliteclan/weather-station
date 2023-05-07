package com.ctrlaltelite.weatherstation.data.service;

import com.ctrlaltelite.weatherstation.data.entity.WeatherEntry;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class WeatherEntryService {

    private final WeatherEntryRepository repository;

    public WeatherEntryService(WeatherEntryRepository repository) {
        this.repository = repository;
    }

    public Optional<WeatherEntry> get(Long id) {
        return repository.findById(id);
    }

    public WeatherEntry update(WeatherEntry entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<WeatherEntry> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<WeatherEntry> list(Pageable pageable, Specification<WeatherEntry> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
