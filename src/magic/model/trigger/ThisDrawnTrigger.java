package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;

public abstract class ThisDrawnTrigger extends MagicTrigger<MagicCard> {
    public ThisDrawnTrigger(final int priority) {
        super(priority);
    }

    public ThisDrawnTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDrawn;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
