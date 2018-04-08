package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;

public class PutStateTriggerOnStackAction extends MagicAction {

    private final MagicEvent event;

    public PutStateTriggerOnStackAction(final MagicEvent aEvent) {
        event = aEvent;
    }

    @Override
    public boolean isLegal(final MagicGame game) {
        return !game.hasItem(event.getSource(), event.getChoiceDescription());
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new EnqueueTriggerAction(event));
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}
