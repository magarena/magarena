package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;

public class MagicAddEventAction extends MagicAction {

	private final MagicEvent event;

	public MagicAddEventAction(final MagicEvent event) {
		this.event=event;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		game.getEvents().addLast(event);
	}

	@Override
	public void undoAction(final MagicGame game) {
		game.getEvents().removeLast();
	}
}
