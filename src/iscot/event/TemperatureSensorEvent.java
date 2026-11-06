package iscot.event;

import iscot.enums.Areas;

// Classe que representa eventos que contêm sinais emitidos pelos sensores.
public class TemperatureSensorEvent extends SensorEvent {

    public TemperatureSensorEvent(Areas area, Double value) {
        super(area, value);
    }
  
}
