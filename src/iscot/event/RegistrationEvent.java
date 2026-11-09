package iscot.event;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {
    private Type type;                                    // Tipo de cliente remoto enum Type

    // Construtor usando a classe topo (Event) e adicionando o Type, uma vez que esta classe serve para
    // registar o tipo de evento
    public RegistrationEvent(Areas area, Type type) {
        super(area);
        this.type = type;
    }

    // Getter do enum Type
    public Type getType() {
        return type;
    }

    // To String
    @Override
    public String toString() {
        return "Cliente " + type + "na área " + getArea();
    }
}
