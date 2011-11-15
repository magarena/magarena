package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.trigger.MagicEchoTrigger;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Hunting_Moa {
	public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    player + " puts a +1/+1 counter on target creature$.");
		}	
		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                	game.doAction(new MagicChangeCountersAction(
        					creature,
        					MagicCounterType.PlusOne,
        					1,
        					true));
                }
            });
		}
    };
	
	public static final MagicWhenPutIntoGraveyardTrigger T3 = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer player = permanent.getController();
			return (MagicLocationType.Play == triggerData.fromLocation) ?
				new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    player + " puts a +1/+1 counter on target creature$.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                	game.doAction(new MagicChangeCountersAction(
        					creature,
        					MagicCounterType.PlusOne,
        					1,
        					true));
                }
            });
		}
    };
}
