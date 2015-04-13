package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.CopyCardOnStackAction;
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
        final int count = game.getSpellsCast();
        return (count > 0) ?
            new MagicEvent(
                cardOnStack,
                count,
                this,
                "Copy SN " + count +
                (count == 1 ? " time." : " times.")
            ) :
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final int count = event.getRefInt();
        for (int i = 0; i < count; i++) {
            game.doAction(new CopyCardOnStackAction(
                event.getPlayer(),
                event.getCardOnStack()
            ));
        }
    }
}
