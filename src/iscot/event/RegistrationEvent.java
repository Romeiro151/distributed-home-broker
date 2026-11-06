package iscot.event;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {
    private static final long serialVersionUID = 1L;
    private Type type;
    private String ID;

    // Construtor de todas as variáveis
    public RegistrationEvent(Areas area, Type type, String ID) {
        super(area);
        this.type = type;
        this.ID = ID;
    }

    // Getter do enum Type
    public Type getType() {
        return type;
    }

    // Getter do ID
    public String getID() {
        return ID;
    }
}
