package iscot.event;

import iscot.enums.Areas;

public abstract class HighOrderLevelEvent extends Event {
    private static final long serialVersionUID = 1L;

    // Construtor
    public HighOrderLevelEvent(Areas area) {
        super(area);
    }
}
