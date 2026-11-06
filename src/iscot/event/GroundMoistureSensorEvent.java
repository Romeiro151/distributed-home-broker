package iscot.event;

import iscot.enums.Areas;

// Classe que representa eventos que contêm sinais emitidos pelos sensores. 
public class GroundMoistureSensorEvent extends SensorEvent {

    public GroundMoistureSensorEvent(Areas area, Double value) {
        super(area, value);
    }

}
