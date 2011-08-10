package magic.card;

import magic.model.*;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.target.MagicBounceTargetPicker;

public class Angelic_Shield {

	public static final MagicPermanentActivation V2216 =new MagicPermanentActivation(			"Angelic Shield",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Return") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE,
                    MagicBounceTargetPicker.getInstance(),
				    MagicEvent.NO_DATA,
                    this,
                    "Return target creature$ to its owner's hand.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
		}
	};
	
}
