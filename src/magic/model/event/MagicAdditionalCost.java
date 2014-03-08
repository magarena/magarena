package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;

public abstract class MagicAdditionalCost implements MagicChangeCardDefinition, MagicEventSource {

    public MagicAdditionalCost() {}

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addCostEvent(this);
    }

    public static MagicAdditionalCost create(final MagicMatchedCostEvent mcEvent) {
        return new MagicAdditionalCost() {
            @Override
            public MagicEvent getEvent(final MagicSource source) {
                return mcEvent.getEvent(source);
            }
        };
    }
}
