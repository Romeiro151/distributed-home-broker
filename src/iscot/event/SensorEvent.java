package iscot.event;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class SensorEvent extends Event {

    // Construtor usando a classe de topo (Event)
    public SensorEvent(Areas area) {
        super(area);
    }

    // Obter o Tipo do cliente (Neste caso é sensor)
    public Type getType(){
        return Type.SENSOR;
    }
  
}
