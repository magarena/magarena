package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicChangeStateAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicPermanentState state;
	private final boolean set;
	private boolean changed;
	
	public MagicChangeStateAction(final MagicPermanent permanent,final MagicPermanentState state,final boolean set) {
		
		this.permanent=permanent;
		this.state=state;
		this.set=set;
	}
	
	@Override
	public void doAction(final MagicGame game) {

		changed=permanent.hasState(state)!=set;
		if (changed) {
			if (set) {
				permanent.setState(state);
			} else {
				permanent.clearState(state);
			}
			game.setStateCheckRequired();
		}
	}

	@Override
	public void undoAction(final MagicGame game) {

		if (changed) {
			if (set) {
				permanent.clearState(state);				
			} else {
				permanent.setState(state);
			}
		}
	}
}