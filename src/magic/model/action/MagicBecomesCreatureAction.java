package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.variable.MagicLocalVariable;

public class MagicBecomesCreatureAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicLocalVariable localVariable;
	private boolean oldState;
	
	public MagicBecomesCreatureAction(final MagicPermanent permanent,final MagicLocalVariable localVariable) {
		this.permanent=permanent;
		this.localVariable=localVariable;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		oldState=permanent.hasState(MagicPermanentState.Animated);
		if (!oldState) {
			permanent.setState(MagicPermanentState.Animated);
		}
		permanent.addTurnLocalVariable(localVariable);
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (!oldState) {
			permanent.clearState(MagicPermanentState.Animated);
		}
		permanent.removeTurnLocalVariable();
	}
}
