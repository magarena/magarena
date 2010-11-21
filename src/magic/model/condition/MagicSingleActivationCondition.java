package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;

public class MagicSingleActivationCondition implements MagicCondition {

	private MagicActivation activation;

	/** This must be set by the activation. */
	public void setActivation(final MagicActivation activation) {
		
		this.activation=activation;
	}

	@Override
	public boolean accept(final MagicGame game,final MagicSource source) {

		return !game.getStack().hasActivationOnTop(source,activation);
	}
}