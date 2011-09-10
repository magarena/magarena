package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicSource;

public class MagicSingleActivationCondition implements MagicCondition {

	private long actId;

	/** This must be set by the activation. */
	public void setActivation(final long aActId) {
		this.actId = aActId;
	}

	@Override
	public boolean accept(final MagicGame game,final MagicSource source) {
		return !game.getStack().hasActivationOnTop(source,actId);
	}
}
