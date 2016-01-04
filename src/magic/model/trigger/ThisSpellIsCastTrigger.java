package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCardDefinition;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public abstract class ThisSpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public ThisSpellIsCastTrigger(final int priority) {
        super(priority);
    }

    public ThisSpellIsCastTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSpellIsCast;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }

    public static final ThisSpellIsCastTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisSpellIsCastTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
                return sourceEvent.getTriggerEvent(spell);
            }
        };
    }
}
