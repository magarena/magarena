package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicReplicateTrigger extends MagicWhenSpellIsCastTrigger {

    private static final MagicReplicateTrigger INSTANCE = new MagicReplicateTrigger();

    private MagicReplicateTrigger() {}

    public static final MagicReplicateTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicCardOnStack cardOnStack) {
        int kickerCount = (Integer)cardOnStack.getChoiceResults()[1];
        return (kickerCount > 0) ?
            new MagicEvent(
                cardOnStack.getSource(),
                cardOnStack.getController(),
                new Object[]{cardOnStack},
                this,
                "Copy " + cardOnStack.getSource() + " for each time its replicate cost was paid.") :
            MagicEvent.NONE;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
        int kickerCount = (Integer)cardOnStack.getChoiceResults()[1];
        for (;kickerCount>0;kickerCount--) {
            game.doAction(new MagicCopyCardOnStackAction(
                    cardOnStack.getController(),
                    cardOnStack));
        }
    }
}
