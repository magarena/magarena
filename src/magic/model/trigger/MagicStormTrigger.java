package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicStormTrigger extends MagicWhenSpellIsCastTrigger {

    private static final MagicStormTrigger INSTANCE = new MagicStormTrigger();

    private MagicStormTrigger() {}

    public static final MagicStormTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicCardOnStack cardOnStack) {
        final int count = game.getSpellsPlayed();
        return (count > 0) ?
            new MagicEvent(
                cardOnStack,
                new Object[]{count},
                this,
                "Copy SN " + count + 
                (count == 1 ? " time." : " times.")
            ) :
            MagicEvent.NONE;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        final int count = (Integer)data[0];
        for (int i = 0; i < count; i++) {
            game.doAction(new MagicCopyCardOnStackAction(
                event.getPlayer(),
                event.getCardOnStack()
            ));
        }
    }
}
