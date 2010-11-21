package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

public class MagicDeclareAttackerAction extends MagicAction {

	private final MagicPermanent attacker;
	private boolean tap;
	
	public MagicDeclareAttackerAction(final MagicPermanent attacker) {
		
		this.attacker=attacker;
	}

	@Override
	public void doAction(final MagicGame game) {

		attacker.setState(MagicPermanentState.Attacking);
		tap=!attacker.hasAbility(game,MagicAbility.Vigilance)&&!attacker.isTapped();
		if (tap) {
			attacker.setState(MagicPermanentState.Tapped);
		}		
		game.executeTrigger(MagicTriggerType.WhenAttacks,attacker);
	}

	@Override
	public void undoAction(final MagicGame game) {

		attacker.clearState(MagicPermanentState.Attacking);
		if (tap) {
			attacker.clearState(MagicPermanentState.Tapped);
		}
	}
}