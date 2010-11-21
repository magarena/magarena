package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicDeclareAttackersResult;

public class MagicDeclareAttackersAction extends MagicAction {

	private final MagicPlayer player;
	private final MagicDeclareAttackersResult result;
	private int oldAttackers;
	
	public MagicDeclareAttackersAction(final MagicPlayer player,final MagicDeclareAttackersResult result) {
		
		this.player=player;
		this.result=result;
	}
	
	@Override
	public void doAction(final MagicGame game) {

		oldAttackers=player.getNrOfAttackers();
		player.setNrOfAttackers(result.size());
		
		for (final MagicPermanent attacker : result) {
			
			game.doAction(new MagicDeclareAttackerAction(attacker));
		}		
	}

	@Override
	public void undoAction(final MagicGame game) {

		player.setNrOfAttackers(oldAttackers);
	}
}