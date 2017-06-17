package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCardDefinition;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.CopyCardOnStackAction;

public abstract class ThisSpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public ThisSpellIsCastTrigger(final int priority) {
        super(priority);
    }

    public ThisSpellIsCastTrigger() {}

    @Override
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

    public static final ThisSpellIsCastTrigger Replicate = Copy("PN copies SN for each time its replicate cost was paid.");

    public static final ThisSpellIsCastTrigger Conspire = Copy("PN copies SN. PN may choose new targets for the copy.");

    private static final ThisSpellIsCastTrigger Copy(final String desc) {
        return new ThisSpellIsCastTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
                final int kickerCount = cardOnStack.getKicker();
                return (kickerCount > 0) ?
                    new MagicEvent(
                        cardOnStack,
                        this,
                        desc
                    ):
                    MagicEvent.NONE;
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicCardOnStack cardOnStack = event.getCardOnStack();
                final int kickerCount = cardOnStack.getKicker();
                for (int i = 0; i < kickerCount; i++) {
                    game.doAction(new CopyCardOnStackAction(
                        event.getPlayer(),
                        cardOnStack
                    ));
                }
            }
        };
    }
}
