package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicStatic;
import magic.model.action.MagicSetAbilityAction;

public class MagicPlayCardAction extends MagicPutIntoPlayAction {

	public static final int NONE=0;
	public static final int PERSIST=1;
	public static final int REMOVE_AT_END_OF_TURN=2;
	public static final int HASTE_REMOVE_AT_END_OF_YOUR_TURN=3;
	public static final int HASTE_SACRIFICE_AT_END_OF_TURN=4;
	public static final int TAPPED_ATTACKING=5;
	public static final int TAPPED=6;
	public static final int HASTE_UEOT_REMOVE_AT_END_OF_TURN=7;
	public static final int UNDYING = 8;
	
	private final MagicCard card;
	private final MagicPlayer controller;
	private final int action;
	
	public MagicPlayCardAction(final MagicCard card,final MagicPlayer controller,final int action) {
		this.card=card;
		this.controller=controller;
		this.action=action;
	}

	@Override
	protected MagicPermanent createPermanent(final MagicGame game) {
		final MagicPermanent permanent=game.createPermanent(card,controller);
		switch (action) {
			case PERSIST:
				permanent.changeCounters(MagicCounterType.MinusOne,1);
				break;
			case REMOVE_AT_END_OF_TURN:
				permanent.setState(MagicPermanentState.RemoveAtEndOfTurn);
				break;
			case HASTE_REMOVE_AT_END_OF_YOUR_TURN:
				game.doAction(new MagicSetAbilityAction(permanent, MagicAbility.Haste, MagicStatic.Forever)); 
				permanent.setState(MagicPermanentState.RemoveAtEndOfYourTurn);
				break;
			case HASTE_SACRIFICE_AT_END_OF_TURN:
				game.doAction(new MagicSetAbilityAction(permanent, MagicAbility.Haste, MagicStatic.Forever)); 
				permanent.setState(MagicPermanentState.SacrificeAtEndOfTurn);
				break;
			case TAPPED_ATTACKING:
				permanent.setState(MagicPermanentState.Tapped);
				permanent.setState(MagicPermanentState.Attacking);
				break;
			case TAPPED:
				permanent.setState(MagicPermanentState.Tapped);
				break;
			case HASTE_UEOT_REMOVE_AT_END_OF_TURN:
				game.doAction(new MagicSetAbilityAction(permanent, MagicAbility.Haste)); 
				permanent.setState(MagicPermanentState.RemoveAtEndOfTurn);
				break;
			case UNDYING:
				permanent.changeCounters(MagicCounterType.PlusOne,1);
				break;
		}
		return permanent;
	}
}
