package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.choice.MagicCombatCreature;
import magic.model.choice.MagicDeclareBlockersResult;

public class MagicDeclareBlockersAction extends MagicAction {

	private final MagicDeclareBlockersResult result;
	
	public MagicDeclareBlockersAction(final MagicDeclareBlockersResult result) {
		
		this.result=result;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		
		for (final MagicCombatCreature creatures[] : result) {
			
			if (creatures.length>1) {
				final MagicPermanent attacker=creatures[0].permanent;
				for (int index=1;index<creatures.length;index++) {
				
					game.doAction(new MagicDeclareBlockerAction(attacker,creatures[index].permanent));
				}
			}
		}
	}
	
	@Override
	public void undoAction(final MagicGame game) {
		
	}
}