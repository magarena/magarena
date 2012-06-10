package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicCleanupCreatureAction extends MagicAction {

	private final MagicPermanent permanent;
	private int oldAbilityPlayedThisTurn;
	private int oldDamage;
	private int oldPreventDamage;
	private int oldStateFlags;

	MagicCleanupCreatureAction(final MagicPermanent permanent) {
		this.permanent=permanent;
	}

	@Override
	public void doAction(final MagicGame game) {
		oldAbilityPlayedThisTurn=permanent.getAbilityPlayedThisTurn();
		permanent.setAbilityPlayedThisTurn(0);
		oldDamage=permanent.getDamage();
		permanent.setDamage(0);
		oldPreventDamage=permanent.getPreventDamage();
		permanent.setPreventDamage(0);
		oldStateFlags=permanent.getStateFlags();
		permanent.setStateFlags(oldStateFlags&MagicPermanentState.CLEANUP_MASK);
	}

	@Override
	public void undoAction(final MagicGame game) {
		permanent.setAbilityPlayedThisTurn(oldAbilityPlayedThisTurn);
		permanent.setDamage(oldDamage);
		permanent.setPreventDamage(oldPreventDamage);
		permanent.setStateFlags(oldStateFlags);
	}
}
