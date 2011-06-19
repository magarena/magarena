package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicCleanupPermanentAction extends MagicAction {

	private final MagicPermanent permanent;
	private int oldAbilityPlayedThisTurn;
	private int oldStateFlags;
	
	public MagicCleanupPermanentAction(final MagicPermanent permanent) {
		this.permanent=permanent;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		oldAbilityPlayedThisTurn=permanent.getAbilityPlayedThisTurn();
		permanent.setAbilityPlayedThisTurn(0);
		oldStateFlags=permanent.getStateFlags();
		permanent.setStateFlags(oldStateFlags & MagicPermanentState.CLEANUP_MASK);
	}

	@Override
	public void undoAction(final MagicGame game) {
		permanent.setAbilityPlayedThisTurn(oldAbilityPlayedThisTurn);
		permanent.setStateFlags(oldStateFlags);		
	}
}
