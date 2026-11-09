package iscot.event;

import iscot.enums.Areas;

// Classe que representa a leitura dos eventos e emitirão ordens de alto nível.
public class AreaIrrigationOrderEvent extends HighOrderLevelEvent {
    private final boolean activate;                           // Variável para ver se está ativo

    // Construtor usando a classe pai (HighOrderLevelEvent) e do estado do cliente
    public AreaIrrigationOrderEvent(Areas area, boolean activate) {
        super(area);
        this.activate = activate;
    }

    // Getter da variável isActivate
    public boolean isActivate() {
        return activate;
    }

    // To String
    @Override
    public String toString(){
        return "Irrigation - [" + super.toString() + "] - Action: " + (activate ? "TURN ON" : "TURN OFF");
    }
}
