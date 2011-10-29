package magic.card;

import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;
import magic.model.event.MagicWeakenCreatureActivation;

public class Fume_Spitter {
	public static final MagicPermanentActivation A1 = new MagicWeakenCreatureActivation(
            MagicActivation.NO_COND,
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}
	};
}
