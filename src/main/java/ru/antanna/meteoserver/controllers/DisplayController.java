package ru.antanna.meteoserver.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.antanna.meteoserver.models.Measurement;
import ru.antanna.meteoserver.services.MeasurementService;

import java.time.ZoneId;
import java.util.List;

@Controller
public class DisplayController {

    private final MeasurementService measurementService;
    private final ZoneId displayZone;

    public DisplayController(MeasurementService measurementService,
                             @Value("${app.display-zone:Europe/Moscow}") String displayZone) {
        this.measurementService = measurementService;
        this.displayZone = ZoneId.of(displayZone);
    }

    @GetMapping("/")
    public String index(Model model) {
        Measurement last = measurementService.findLast().orElse(null);
        List<Measurement> recent = measurementService.find50Last();

        model.addAttribute("last", last);
        model.addAttribute("measurements", recent);
        model.addAttribute("zoneId", displayZone);

        return "index";
    }
}
