package iscot.event;

import iscot.enums.Areas;
import iscot.enums.AirConditioningMode;

// Classe que representa a leitura dos eventos e emitirão ordens de alto nível.
public class AreaTemperatureOrderEvent extends HighOrderLevelEvent {
  
    private  AirConditioningMode mode;

    public AreaTemperatureOrderEvent(Areas area, AirConditioningMode mode) {
        super(area);
        this.mode = mode;
    }

    public AirConditioningMode getMode() {
        return mode;
    }
}
