package iscot.event;

import iscot.enums.Areas;

public abstract class SensorEvent extends Event {
    private static final long serialVersionUID = 1L;
    private double value;

    // Construtor de todas as variáveis
    public SensorEvent(Areas area, Double value) {
        super(area);
        this.value = value;
    }

    // Getter do valor
    public double getValue() {
        return value;
    }
  
}
