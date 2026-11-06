error id: file:///C:/Users/Utilizador/Downloads/ProjetoPCD_26_27_IscoT_GUI/src/iscot/event/Event.java:iscot/event/Event#serialVersionUID.
file:///C:/Users/Utilizador/Downloads/ProjetoPCD_26_27_IscoT_GUI/src/iscot/event/Event.java
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol iscot/event/Event#serialVersionUID.
empty definition using fallback
non-local guesses:

offset: 161
uri: file:///C:/Users/Utilizador/Downloads/ProjetoPCD_26_27_IscoT_GUI/src/iscot/event/Event.java
text:
```scala
package iscot.event;

import iscot.enums.Areas;
package iscot.enums;

public class Event implements Serializable {
    private static final long serialVersionUID@@ = 1L;
    private Areas area;
    
    // Contrutor da classe mãe Event
    public Event(Areas area) {
        this.area = area;
    }
    

    public Areas getArea() {
        return area;
    }  
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 