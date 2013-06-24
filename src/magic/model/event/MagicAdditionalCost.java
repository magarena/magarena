package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;

public abstract class MagicAdditionalCost implements MagicChangeCardDefinition, MagicEventSource {

    public MagicAdditionalCost() {}

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addCostEvent(this);
    }
}
