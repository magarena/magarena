package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

public class MagicDeclareBlockerAction extends MagicAction {

	private final MagicPermanent attacker;
	private final MagicPermanent blocker;
	private boolean unblocked;
	
	public MagicDeclareBlockerAction(final MagicPermanent attacker,final MagicPermanent blocker) {
		this.attacker=attacker;
		this.blocker=blocker;
	}

	@Override
	public void doAction(final MagicGame game) {
		attacker.addBlockingCreature(blocker);
		blocker.setBlockedCreature(attacker);
		blocker.setState(MagicPermanentState.Blocking);
		unblocked=!attacker.hasState(MagicPermanentState.Blocked);
		if (unblocked) {
			attacker.setState(MagicPermanentState.Blocked);
		}
		game.executeTrigger(MagicTriggerType.WhenBlocks,blocker);
	}
	
	@Override
	public void undoAction(final MagicGame game) {
		attacker.removeBlockingCreature(blocker);
		blocker.setBlockedCreature(null);
		blocker.clearState(MagicPermanentState.Blocking);
		if (unblocked) {
			attacker.clearState(MagicPermanentState.Blocked);
		}
	}
}
