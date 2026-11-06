package iscot.event;

import iscot.enums.Areas;

// Classe que representa a leitura dos eventos e emitirão ordens de alto nível.
public class AreaIrrigationOrderEvent extends HighOrderLevelEvent {
    private boolean activate;

    public AreaIrrigationOrderEvent(Areas area, boolean activate) {
        super(area);
        this.activate = activate;
    }

    public boolean isActivate() {
        return activate;
    }
  
}
