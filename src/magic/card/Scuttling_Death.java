package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicActivation;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.trigger.MagicSoulshiftTrigger;

public class Scuttling_Death {
	public static final MagicSoulshiftTrigger T = new MagicSoulshiftTrigger(4);
	
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicActivation.NO_COND,
            new MagicActivationHints(MagicTiming.Removal),
            "Weaken") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(1,1),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets -1/-1 until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
                }
			});
		}
	};
}
