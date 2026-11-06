package iscot.event;

import iscot.enums.Areas;

// Classe que representa a leitura dos eventos e emitirão ordens de alto nível.
public class AreaLightingOrderEvent extends HighOrderLevelEvent {
    private boolean isOn;

    public AreaLightingOrderEvent(Areas area, boolean isOn) {
        super(area);
        this.isOn = isOn;
    }

    public boolean isOn() {
        return isOn;
    }
}
