package magic.model.action;

import magic.model.MagicGame;

public class MagicLogMarkerAction extends MagicAction {

	private int oldSize;
	
	@Override
	public void doAction(final MagicGame game) {
		
		oldSize=game.getLogBook().size();
	}

	@Override
	public void undoAction(final MagicGame game) {

		game.clearMessages();
		game.getLogBook().removeTo(oldSize);
	}
}