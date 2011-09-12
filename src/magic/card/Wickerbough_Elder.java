package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Wickerbough_Elder {
	public static final MagicPermanentActivation A = new MagicPermanentActivation( 
			new MagicCondition[]{
                MagicCondition.MINUS_COUNTER_CONDITION,
                MagicManaCost.GREEN.getCondition()
            },
            new MagicActivationHints(MagicTiming.Removal),
            "destroy") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.GREEN),
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.MinusOne,1)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                    new MagicDestroyTargetPicker(false),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target artifact or enchantment$.");
		}
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                }
			});
		}
	};

    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
					permanent + " enters the battlefield with a -1/-1 counter on it.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.MinusOne,1,false));
		}
		@Override
		public boolean usesStack() {
			return false;
		}
    };
}
