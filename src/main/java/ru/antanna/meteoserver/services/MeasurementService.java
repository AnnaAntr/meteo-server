package ru.antanna.meteoserver.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.antanna.meteoserver.dto.MeasurementRequest;
import ru.antanna.meteoserver.models.Measurement;
import ru.antanna.meteoserver.repositories.MeasurementsRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MeasurementService {

    private final MeasurementsRepository measurementsRepository;

    public MeasurementService(MeasurementsRepository measurementsRepository) {
        this.measurementsRepository = measurementsRepository;
    }

    public Measurement save(MeasurementRequest request) {
        Measurement measurement = new Measurement();

        String deviceId = request.getDeviceId();
        if (deviceId != null && !deviceId.isBlank()) {
            measurement.setDeviceId(deviceId.trim());
        }

        measurement.setTemperature(request.getTemperature());
        measurement.setHumidity(request.getHumidity());
        measurement.setReceivedAt(Instant.now());

        return measurementsRepository.save(measurement);
    }

    public List<Measurement> findForPeriod(Instant from, Instant to) {
        return measurementsRepository.findByReceivedAtBetweenOrderByReceivedAtAsc(from, to);
    }
    
    public List<Measurement> find50Last() {
        return measurementsRepository.findTop50ByOrderByReceivedAtDesc();
    }
    
    public Optional<Measurement> findLast() {
        return measurementsRepository.findFirstByOrderByReceivedAtDesc();
    }

    public List<Measurement> findRecent() {
        return measurementsRepository.findTop50ByOrderByReceivedAtDesc();
    }
}
