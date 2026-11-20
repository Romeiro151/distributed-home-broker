package iscot.event;

import iscot.enums.Areas;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class Event implements Serializable {
    private Areas area;
    private LocalDateTime timestamp;
    private static final long serialversionID = 1L;
    
    // Contrutor da classe mãe Event
    public Event(Areas area) {
        this.area = area;
        this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
    }
    
    // Getter o enum Areas
    public Areas getArea() {
        return area;
    }

    //Getter da timestamp
    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    //To String
    @Override
    public String toString(){
        return timestamp.toLocalTime() +" - Area: " + area;
    }
}
