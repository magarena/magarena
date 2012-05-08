package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTrigger;

public class MagicAddTurnTriggerAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicTrigger<?> trigger;
	private MagicPermanentTrigger permanentTrigger;
	
	public MagicAddTurnTriggerAction(final MagicPermanent permanent,final MagicTrigger<?> trigger) {
		this.permanent=permanent;
		this.trigger=trigger;
	}

	@Override
	public void doAction(final MagicGame game) {
		permanentTrigger=game.addTurnTrigger(permanent,trigger);
	}

	@Override
	public void undoAction(final MagicGame game) {
		game.removeTurnTrigger(permanentTrigger);
	}
}
