package magic.model.action;

import magic.model.MagicGame;

public class MagicChangeLandPlayedAction extends MagicAction {

	private final int change;
	
	public MagicChangeLandPlayedAction(final int change) {
		this.change = change;
	}
	
	@Override
	public void doAction(final MagicGame game) {
        game.decLandPlayed();
	}

	@Override
	public void undoAction(final MagicGame game) {

	}
}
