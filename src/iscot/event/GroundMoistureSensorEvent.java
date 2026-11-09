package iscot.event;

import iscot.enums.Areas;

// Classe que representa eventos que contêm sinais emitidos pelos sensores. 
public class GroundMoistureSensorEvent extends SensorEvent {
    private final double value;

    // Construtor usando a classe pai (SensorEvent) e de um valor que vai ser lido pelo sensor
    public GroundMoistureSensorEvent(Areas area, Double value) {
        super(area);
        this.value = value;
    }

    // Getter do valor lido pelo sensor
    public double getValue(){
        return value;
    }

    //To String

    @Override
    public String toString() {
        return "Ground Moisture -[" + super.toString() +"] - Leu: " + getValue() + "%";
    }
}
