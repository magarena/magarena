package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;

public class MagicAddFirstEventAction extends MagicAction {

    private final MagicEvent event;

    public MagicAddFirstEventAction(final MagicEvent aEvent) {
        event = aEvent;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.getEvents().addFirst(event);
    }

    @Override
    public void undoAction(final MagicGame game) {
        final MagicEvent firstEvent = game.getEvents().removeFirst();
        assert firstEvent == event : "removed event " + firstEvent + " different from added event " + event;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + event + ')';
    }
}
