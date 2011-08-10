package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Vampire_Hexmage {

	public static final MagicPermanentActivation V2100 =new MagicPermanentActivation(			"Vampire Hexmage",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Remove") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicCountersTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Remove all counters from target permanent$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				for (final MagicCounterType counterType : MagicCounterType.values()) {
					final int amount=permanent.getCounters(counterType);
					if (amount>0) {
						game.doAction(new MagicChangeCountersAction(permanent,counterType,-amount,true));
					}
				}
			}
		}
	};
	
}
