package magic.card;

import magic.model.*;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.condition.MagicCondition;
import magic.model.action.MagicPermanentAction;

public class Angelic_Shield {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicCondition.NONE,
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
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
                }
			});
		}
	};
}
