package iscot.event;

import iscot.enums.Areas;

// Classe que representa eventos que contêm sinais emitidos pelos sensores.
public class TemperatureSensorEvent extends SensorEvent {
    private final double value;

    // Contrutor usando a classe pai (SensorEvent) e um valor que vai ser lido pelo sensor
    public TemperatureSensorEvent(Areas area, Double value) {
        super(area);
        this.value = value;
    }

    // Getter do valor lido pelo sensor
    public double getValue() {
        return value;
    }

    // To String
    @Override
    public String toString() {
        return "Temperature Sensor -[" + super.toString() +"] - Leu: " + getValue() + "%";
    }
}
