package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;

public class Kazandu_Tuskcaller {
	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.TWO_CHARGE_COUNTERS_CONDITION,
                MagicCondition.CAN_TAP_CONDITION,
			},
			new MagicActivationHints(MagicTiming.Token),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			final int amount = source.getCounters(MagicCounterType.Charge);
			final String description = amount >= 6 ?
					" puts two 3/3 green Elephant creature tokens onto the battlefield." :
					" puts a 3/3 green Elephant creature token onto the battlefield.";
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{amount,player},
                    this,
					player + description);
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(
					player,
					TokenCardDefinitions.get("Elephant")));
			if ((Integer)data[0] >= 6) {
				game.doAction(new MagicPlayTokenAction(
						player,
						TokenCardDefinitions.get("Elephant")));
			}
		}
	};
}
