package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class MagicExileUntilThisLeavesPlayAction extends MagicAction {

	private final MagicPermanent source;
	private final MagicPermanent permanent;
	
	public MagicExileUntilThisLeavesPlayAction(final MagicPermanent source,final MagicPermanent permanent) {
		this.source = source;
		this.permanent = permanent;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
		source.addExiledCard(permanent.getCard());
	}

	@Override
	public void undoAction(final MagicGame game) {
		source.removeExiledCard(permanent.getCard());
	}
}