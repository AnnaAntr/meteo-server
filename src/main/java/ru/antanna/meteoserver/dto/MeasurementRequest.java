package ru.antanna.meteoserver.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MeasurementRequest {

    @Size(max = 64, message = "Идентификатор устройства не должен быть длиннее 64 символов")
    private String deviceId;

    @NotNull(message = "Температура обязательна")
    @DecimalMin(value = "-40.0", message = "Температура должна быть не ниже -40 °C")
    @DecimalMax(value = "80.0", message = "Температура должна быть не выше 80 °C")
    private Double temperature;

    @NotNull(message = "Влажность обязательна")
    @DecimalMin(value = "0.0", message = "Влажность должна быть не ниже 0 %")
    @DecimalMax(value = "100.0", message = "Влажность должна быть не выше 100 %")
    private Double humidity;

    public MeasurementRequest() {}

    public MeasurementRequest(String deviceId, Double temperature, Double humidity) {
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
}
