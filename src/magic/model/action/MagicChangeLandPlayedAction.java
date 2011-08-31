package magic.model.action;

import magic.model.MagicGame;

public class MagicChangeLandPlayedAction extends MagicAction {

	private final int change;
	
	public MagicChangeLandPlayedAction(final int change) {
		this.change = change;
	}
	
	@Override
	public void doAction(final MagicGame game) {
        for (int i = change; i != 0; i -= Integer.signum(i)) {
            if (i < 0) {
                game.decLandPlayed();
            } else {
                game.incLandPlayed();
            }
        }
	}

	@Override
	public void undoAction(final MagicGame game) {
        //undo done by MagicMarkerAction
	}
}
