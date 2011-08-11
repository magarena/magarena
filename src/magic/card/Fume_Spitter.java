package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.target.MagicWeakenTargetPicker;

public class Fume_Spitter {
	public static final MagicPermanentActivation A1 = new MagicWeakenCreatureActivation(
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}
	};
}
