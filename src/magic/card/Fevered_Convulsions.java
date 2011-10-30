package magic.card;

import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.event.MagicWeakenCreatureActivation;

public class Fevered_Convulsions {
	public static final MagicPermanentActivation A = new MagicWeakenCreatureActivation(
			new MagicCondition[]{MagicManaCost.TWO_BLACK_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal,true),
            "-1/-1") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(
					source,
					source.getController(),
					MagicManaCost.TWO_BLACK_BLACK)};
		}
	};
}
