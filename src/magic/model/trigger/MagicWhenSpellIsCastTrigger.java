package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCardDefinition;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.event.MagicEvent;
import magic.model.action.ChangeCardDestinationAction;
import magic.model.action.AddTriggerAction;

public abstract class MagicWhenSpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public MagicWhenSpellIsCastTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenSpellIsCastTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSpellIsCast;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
