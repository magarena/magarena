package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.mstatic.MagicStatic;

public class MagicBecomesCreatureAction extends MagicAction {

	private final MagicPermanent permanent;
	private final MagicStatic mstatics[];
	private boolean oldState;
	
	public MagicBecomesCreatureAction(final MagicPermanent aPermanent,final MagicStatic... aMstatics) {
        permanent = aPermanent;
        mstatics = aMstatics;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		oldState = permanent.hasState(MagicPermanentState.Animated);
		if (!oldState) {
			permanent.setState(MagicPermanentState.Animated);
		}
        for (final MagicStatic mstatic : mstatics) {
            game.doAction(new MagicAddStaticAction(permanent, mstatic));
        }
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (!oldState) {
			permanent.clearState(MagicPermanentState.Animated);
		}
	}
}
