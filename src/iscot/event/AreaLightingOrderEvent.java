package iscot.event;

import iscot.enums.Areas;

// Classe que representa a leitura dos eventos e emitirão ordens de alto nível.
public class AreaLightingOrderEvent extends HighOrderLevelEvent {
    private boolean isOn;                       // Variável para ver se está ativo

    // Construtor usando a classe pai (HighOrderLevelEvent) e do estado do cliente
    public AreaLightingOrderEvent(Areas area, boolean isOn) {
        super(area);
        this.isOn = isOn;
    }

    // Getter da variável isOn
    public boolean isOn() {
        return isOn;
    }

    // To String
    @Override
    public String toString(){
        return "Lightning - [" + super.toString() + "] - Action: " + (isOn ? "TURN ON" : "TURN OFF");
    }
}
