package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicCombatCreature;
import magic.model.choice.MagicDeclareBlockersResult;
import magic.model.trigger.MagicTriggerType;

public class MagicDeclareBlockersAction extends MagicAction {

	private final MagicPlayer player;
	private final MagicDeclareBlockersResult result;
	private int oldBlockers;
	
	public MagicDeclareBlockersAction(final MagicPlayer player, final MagicDeclareBlockersResult result) {
        this.player = player;
		this.result = result;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		oldBlockers=player.getNrOfBlockers();
        int count = 0;

		for (final MagicCombatCreature creatures[] : result) {
			if (creatures.length>1) {
				final MagicPermanent attacker=creatures[0].permanent;
				for (int index=1;index<creatures.length;index++) {
					game.doAction(new MagicDeclareBlockerAction(attacker,creatures[index].permanent));
                    count++;
				}
				
				if (attacker.isBlocked()) {
					game.executeTrigger(MagicTriggerType.WhenBecomesBlocked,attacker);
				}
			}
		}

		player.setNrOfBlockers(count);
		
		for (final MagicPermanent permanent : game.getOpponent(player).getPermanents()) {	
			if (permanent.isAttacking() && !permanent.isBlocked()) {
				game.executeTrigger(MagicTriggerType.WhenAttacksUnblocked,permanent);
			}
		}
        
        
	}
	
	@Override
	public void undoAction(final MagicGame game) {
		player.setNrOfBlockers(oldBlockers);
	}
}
