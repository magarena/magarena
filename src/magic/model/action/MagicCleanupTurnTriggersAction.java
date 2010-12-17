package magic.model.action;

import java.util.List;

import magic.model.MagicGame;
import magic.model.trigger.MagicPermanentTrigger;

public class MagicCleanupTurnTriggersAction extends MagicAction {

	private List<MagicPermanentTrigger> removedTriggers;
	
	@Override
	public void doAction(final MagicGame game) {
		
		removedTriggers=game.removeTurnTriggers();		
	}

	@Override
	public void undoAction(final MagicGame game) {

		game.addTurnTriggers(removedTriggers);
	}
}