package magic.model.action;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class MagicSetTurnColorAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicColor color;
	private int oldColorFlags;

	public MagicSetTurnColorAction(final MagicPermanent permanent,final MagicColor color) {
		
		this.permanent=permanent;
		this.color=color;
	}	
	
	@Override
	public void doAction(final MagicGame game) {

		oldColorFlags=permanent.getTurnColorFlags();
		permanent.setTurnColorFlags(color.getMask());
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {

		permanent.setTurnColorFlags(oldColorFlags);
	}	
}