package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;

public class PutStateTriggerOnStackAction extends MagicAction {

    private final MagicEvent event;

    public PutStateTriggerOnStackAction(final MagicEvent aEvent) {
        event = aEvent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (game.hasItem(event.getSource(), event.getChoiceDescription()) == false) {
            game.doAction(new EnqueueTriggerAction(event));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}
