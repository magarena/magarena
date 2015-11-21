package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.CopyCardOnStackAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class ReplicateTrigger extends MagicWhenSpellIsCastTrigger {

    private static final ReplicateTrigger INSTANCE = new ReplicateTrigger();

    private ReplicateTrigger() {}

    public static final ReplicateTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
        final int kickerCount = cardOnStack.getKicker();
        return (kickerCount > 0) ?
            new MagicEvent(
                cardOnStack,
                this,
                "Copy SN for each time its replicate cost was paid."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCardOnStack cardOnStack = event.getCardOnStack();
        final int kickerCount = cardOnStack.getKicker();
        for (int i = 0; i < kickerCount; i++) {
            game.doAction(new CopyCardOnStackAction(
                cardOnStack.getController(),
                cardOnStack
            ));
        }
    }
}
