package magic.card;

import magic.model.*;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTapTargetPicker;

public class Gideon_s_Lawkeeper {

	public static final MagicPermanentActivation V980 =new MagicPermanentActivation(			"Gideon's Lawkeeper",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.WHITE.getCondition()},
			new MagicActivationHints(MagicTiming.Tapping),
			"Tap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
					source,
					source.getController(),
					MagicTargetChoice.NEG_TARGET_CREATURE,
					new MagicTapTargetPicker(true,false),
					MagicEvent.NO_DATA,
					this,
					"Tap target creature$.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null&&!creature.isTapped()) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};
				
}
