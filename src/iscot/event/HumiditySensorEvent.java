package iscot.event;

import iscot.enums.Areas;

// Classe que representa eventos que contêm sinais emitidos pelos sensores.
public class HumiditySensorEvent extends SensorEvent {
    private final double value;

    // Construtor usando a classe pai (Sensor Event) e de um valor que vai ser lido pelo sensor
    public HumiditySensorEvent(Areas area, Double value) {
        super(area);
        this.value = value;
    }

    // Getter do valor lido pelo sensor
    public double getValue(){
        return value;
    }

    // To string
    @Override
    public String toString() {
        return "Humidity Sensor -[" + super.toString() +"] - Leu: " + getValue() + "%";
    }

}
