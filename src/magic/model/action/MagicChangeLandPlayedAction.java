package magic.model.action;

import magic.model.MagicGame;

public class MagicChangeLandPlayedAction extends MagicAction {

	private final int change;
	
	public MagicChangeLandPlayedAction(final int change) {
		this.change = change;
	}
	
	@Override
	public void doAction(final MagicGame game) {
        for (int i = change; i < 0; i++) {
            game.decLandPlayed();
        }
        for (int i = change; i > 0; i--) {
            game.incLandPlayed();
        }
	}

	@Override
	public void undoAction(final MagicGame game) {
        //undo done by MagicMarkerAction
	}
}
