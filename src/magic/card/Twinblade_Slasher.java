package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.event.*;

public class Twinblade_Slasher {

	public static final MagicPermanentActivation V2031 =new MagicPermanentActivation(            "Twinblade Slasher",
			new MagicCondition[]{MagicCondition.ABILITY_ONCE_CONDITION,MagicManaCost.ONE_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_GREEN),
                new MagicPlayAbilityEvent(source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Twinblade Slasher gets +2/+2 until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
		}
	};
	
}
