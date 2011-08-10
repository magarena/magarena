package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.variable.MagicLocalVariable;

import java.util.List;

public class MagicCleanupCreatureAction extends MagicAction {

	private final MagicPermanent permanent;
	private long oldTurnAbilityFlags;
	private int oldTurnPower;
	private int oldTurnToughness;
	private int oldTurnColorFlags;
	private int oldAbilityPlayedThisTurn;
	private int oldDamage;
	private int oldPreventDamage;
	private int oldStateFlags;
	private List<MagicLocalVariable> oldTurnLocalVariables;

	public MagicCleanupCreatureAction(final MagicPermanent permanent) {
		
		this.permanent=permanent;
	}

	@Override
	public void doAction(final MagicGame game) {

		oldTurnAbilityFlags=permanent.getTurnAbilityFlags();
		permanent.setTurnAbilityFlags(0);
		oldTurnPower=permanent.getTurnPower();
		permanent.setTurnPower(0);
		oldTurnToughness=permanent.getTurnToughness();
		permanent.setTurnToughness(0);
		oldTurnColorFlags=permanent.getTurnColorFlags();
		permanent.setTurnColorFlags(MagicPermanent.NO_COLOR_FLAGS);
		oldAbilityPlayedThisTurn=permanent.getAbilityPlayedThisTurn();
		permanent.setAbilityPlayedThisTurn(0);
		oldDamage=permanent.getDamage();
		permanent.setDamage(0);
		oldPreventDamage=permanent.getPreventDamage();
		permanent.setPreventDamage(0);
		oldStateFlags=permanent.getStateFlags();
		permanent.setStateFlags(oldStateFlags&MagicPermanentState.CLEANUP_MASK);
		oldTurnLocalVariables=permanent.removeTurnLocalVariables();
	}

	@Override
	public void undoAction(final MagicGame game) {

		permanent.setTurnAbilityFlags(oldTurnAbilityFlags);
		permanent.setTurnPower(oldTurnPower);
		permanent.setTurnToughness(oldTurnToughness);
		permanent.setTurnColorFlags(oldTurnColorFlags);
		permanent.setAbilityPlayedThisTurn(oldAbilityPlayedThisTurn);
		permanent.setDamage(oldDamage);
		permanent.setPreventDamage(oldPreventDamage);
		permanent.setStateFlags(oldStateFlags);
		permanent.addTurnLocalVariables(oldTurnLocalVariables);
	}
}