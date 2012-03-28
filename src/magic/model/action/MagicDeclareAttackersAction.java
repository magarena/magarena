package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicDeclareAttackersResult;

public class MagicDeclareAttackersAction extends MagicAction {

	private final MagicDeclareAttackersResult result;
	
	public MagicDeclareAttackersAction(final MagicDeclareAttackersResult result) {
		this.result=result;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		for (final MagicPermanent attacker : result) {
			game.doAction(new MagicDeclareAttackerAction(attacker));
		}		
	}

	@Override
	public void undoAction(final MagicGame game) {
	}
}
