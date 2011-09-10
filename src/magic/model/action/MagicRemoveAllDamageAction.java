package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class MagicRemoveAllDamageAction extends MagicAction {

	private final MagicPermanent permanent;
	private int oldDamage;
	
	MagicRemoveAllDamageAction(final MagicPermanent permanent) {
		
		this.permanent=permanent;
	}

	@Override
	public void doAction(final MagicGame game) {

		oldDamage=permanent.getDamage();
		permanent.setDamage(0);
	}

	@Override
	public void undoAction(final MagicGame game) {

		permanent.setDamage(oldDamage);
	}
}