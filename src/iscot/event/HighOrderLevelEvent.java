package iscot.event;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class HighOrderLevelEvent extends Event {

    // Construtor usando a classe de topo (Event)
    public HighOrderLevelEvent(Areas area) {
        super(area);
    }

    // Obter o tipo de cliente (Neste caso Decisor)
    public Type getType(){
        return Type.DECIDER;
    }
}
