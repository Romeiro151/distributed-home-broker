package iscot.event;

import iscot.enums.Areas;

// Classe que representa eventos que contêm sinais emitidos pelos sensores.
public class HumiditySensorEvent extends SensorEvent {
    
    public HumiditySensorEvent(Areas area, Double value) {
        super(area, value);
    }

}
