package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;

public abstract class MagicWhenDrawnTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenDrawnTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenDrawnTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDrawn;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
