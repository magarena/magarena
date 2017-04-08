package magic.model.action;

import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

public class DequeueTriggerAction extends MagicAction {

    private MagicItemOnStack trigger;

    public DequeueTriggerAction() {}

    @Override
    public void doAction(final MagicGame game) {
        trigger = game.getPendingTriggers().removeFirst();
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.getPendingTriggers().addFirst(trigger);
    }
}
