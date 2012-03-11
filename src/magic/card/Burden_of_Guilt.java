package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Burden_of_Guilt {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
				MagicCondition.ENCHANTED_IS_UNTAPPED_CONDITION,
                MagicManaCost.ONE.getCondition(),
            },
			new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(
					source,
					source.getController(),
					MagicManaCost.ONE)
			};
		}
		@Override
		public MagicEvent getPermanentEvent(
				final MagicPermanent source,
				final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source.getEnchantedCreature()},
                    this,
                    "Tap " + source.getEnchantedCreature() + ".");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicTapAction((MagicPermanent)data[0],true));
		}
	};
}
