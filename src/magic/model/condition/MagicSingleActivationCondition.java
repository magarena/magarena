package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;

public class MagicSingleActivationCondition implements MagicCondition {

	private MagicActivation act;

	/** This must be set by the activation. */
	public void setActivation(final MagicActivation aAct) {
        act = aAct;
	}

	@Override
	public boolean accept(final MagicGame game,final MagicSource source) {
		return !game.getStack().hasActivationOnTop(source,act);
	}
}
