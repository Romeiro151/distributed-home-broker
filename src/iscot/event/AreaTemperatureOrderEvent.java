package iscot.event;

import iscot.enums.Areas;
import iscot.enums.AirConditioningMode;

// Classe que representa a leitura dos eventos e emitirão ordens de alto nível.
public class AreaTemperatureOrderEvent extends HighOrderLevelEvent {
    private  AirConditioningMode mode;                        // Variável para ver o modo do Enum do AC

    // Construtor usando a classe pai (HighOrderLevelEvent) e do estado do cliente
    public AreaTemperatureOrderEvent(Areas area, AirConditioningMode mode) {
        super(area);
        this.mode = mode;
    }

    // Getter do modo do AC
    public AirConditioningMode getMode() {
        return mode;
    }

    // To String
    @Override
    public String toString(){
        return "Temperature - [" + super.toString() + "] - Action: " + getMode();
    }
}
