package ru.antanna.meteoserver.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.antanna.meteoserver.dto.MeasurementRequest;
import ru.antanna.meteoserver.models.Measurement;
import ru.antanna.meteoserver.services.MeasurementService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ZoneId timeZone;

    @Autowired
    public MeasurementController(MeasurementService measurementService, @Value("${app.time-zone:Europe/Moscow}") String displayZone) {
        this.measurementService = measurementService;
        this.timeZone = ZoneId.of(displayZone);
    }

    @PostMapping("/measurements")
    public ResponseEntity<Measurement> create(@Valid @RequestBody MeasurementRequest request) {
        Measurement saved = measurementService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/measurements")
    public List<Measurement> getForPeriod(
            @RequestParam(defaultValue = "24")
            @Min(value = 1, message = "Период должен быть не менее 1 часа")
            @Max(value = 720, message = "Период должен быть не более 720 часов")
            int hours) {
        
        Instant to = Instant.now();
        Instant from = to.minus(hours, ChronoUnit.HOURS);
        return measurementService.findForPeriod(from, to);
    }

    @GetMapping("/measurements/today")
    public List<Measurement> getToday() {
        return measurementService.findForDay(LocalDate.now(timeZone), timeZone);
    }
}
