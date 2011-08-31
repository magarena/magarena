package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicCountersTargetPicker;

public class Vampire_Hexmage {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicCondition.NONE,
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
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    for (final MagicCounterType counterType : MagicCounterType.values()) {
                        final int amount=permanent.getCounters(counterType);
                        if (amount>0) {
                            game.doAction(new MagicChangeCountersAction(permanent,counterType,-amount,true));
                        }
                    }
                }
			});
		}
	};
}
