package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class MagicReanimateAction extends MagicAction {
		
	private final MagicPlayer controller;
	private final MagicCard card;
	private final int action;
	
	public MagicReanimateAction(final MagicPlayer controller,final MagicCard card,final int action) {
		this.controller=controller;
		this.card=card;
		this.action=action;
	}
	
	@Override
	public void doAction(MagicGame game) {
		if (card.getOwner().getGraveyard().contains(card)) {
			game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
			game.doAction(new MagicPlayCardAction(card,controller,action));
		}
	}

	@Override
	public void undoAction(MagicGame game) {

	}
}
