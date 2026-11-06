package iscot.event;

import iscot.enums.Areas;
import java.io.Serializable;

public abstract class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private Areas area;
    
    // Contrutor da classe mãe Event
    public Event(Areas area) {
        this.area = area;
    }
    
    // Getter o enum Areas
    public Areas getArea() {
        return area;
    }  
}
