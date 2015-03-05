package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCardDefinition;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.event.MagicEvent;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.action.MagicAddTriggerAction;

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

    public static final MagicWhenSpellIsCastTrigger Buyback = new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack spell) {
            if (spell.isKicked()) {
                game.doAction(new MagicChangeCardDestinationAction(spell, MagicLocationType.OwnersHand));
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicWhenSpellIsCastTrigger Rebound = new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack spell) {
            if (spell.getFromLocation() == MagicLocationType.OwnersHand) {
                game.doAction(new MagicChangeCardDestinationAction(spell, MagicLocationType.Exile));
                game.doAction(new MagicAddTriggerAction(new MagicReboundTrigger(spell.getCard())));
            }
            return MagicEvent.NONE;
        }
    };
}
