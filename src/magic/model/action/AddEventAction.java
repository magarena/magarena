package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;

public class AddEventAction extends MagicAction {

    private final MagicEvent event;

    public AddEventAction(final MagicEvent aEvent) {
        event = aEvent;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.getEvents().addLast(event);
    }

    @Override
    public void undoAction(final MagicGame game) {
        final MagicEvent lastEvent = game.getEvents().removeLast();
        assert lastEvent == event : "removed event " + lastEvent + " different from added event " + event;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + event + ')';
    }
}
