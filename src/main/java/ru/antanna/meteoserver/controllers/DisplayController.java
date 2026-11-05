package ru.antanna.meteoserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.antanna.meteoserver.models.Measurement;
import ru.antanna.meteoserver.services.MeasurementService;

import java.time.ZoneId;
import java.util.List;

@Controller
public class DisplayController {

    private static final ZoneId DISPLAY_ZONE = ZoneId.of("Europe/Moscow");

    private final MeasurementService measurementService;

    @Autowired
    public DisplayController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping("/")
    public String index(Model model) {
        Measurement last = measurementService.findLast().orElse(null);
        List<Measurement> recent = measurementService.findRecent();

        model.addAttribute("last", last);
        model.addAttribute("measurements", recent);
        model.addAttribute("zoneId", DISPLAY_ZONE);

        return "index";
    }
}
