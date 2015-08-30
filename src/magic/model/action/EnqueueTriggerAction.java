package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicTriggerOnStack;

public class EnqueueTriggerAction extends MagicAction {

    private final MagicTriggerOnStack trigger;

    public EnqueueTriggerAction(final MagicEvent event) {
        trigger = new MagicTriggerOnStack(event);
    }

    @Override
    public void doAction(final MagicGame game) {
        game.getPendingTriggers().add(trigger);
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.getPendingTriggers().removeLast();
    }
}
