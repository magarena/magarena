package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class Brion_Stoutarm {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,MagicManaCost.RED.getCondition(),
                MagicCondition.TWO_CREATURES_CONDITION
            },
			new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicTargetFilter targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,(MagicPermanent)source);
			final MagicTargetChoice targetChoice=new MagicTargetChoice(
					targetFilter,false,MagicTargetHint.None,"a creature other than " + source + " to sacrifice");
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.RED),
				new MagicSacrificePermanentEvent(source,source.getController(),targetChoice)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicTarget sacrificed=payedCost.getTarget();
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new Object[]{source,sacrificed},
                    this,
                    source + " deals damage equal to the power of "+sacrificed+" to target player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicPermanent sacrificed=(MagicPermanent)data[1];
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],player,sacrificed.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
}
