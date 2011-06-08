package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

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
