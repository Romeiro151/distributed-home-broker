package iscot.event;

import iscot.enums.Areas;

// Classe que representa a leitura dos eventos e emitirão ordens de alto nível.
public class AreaVentilationOrderEvent extends HighOrderLevelEvent {
    private boolean activate;               // Variável para ver se está ativo

    // Construtor usando a classe pai (HighOrderLevelEvent) e do estado do cliente
    public AreaVentilationOrderEvent(Areas area, boolean activate) {
        super(area);
        this.activate = activate;
    }

    // Getter do activate
    public boolean isActivate() {
        return activate;
    }

    // To String
    @Override
    public String toString(){
        return "Ventilation - [" + super.toString() + "] - Action: " + (activate ? "TURN ON" : "TURN OFF");
    }
}
