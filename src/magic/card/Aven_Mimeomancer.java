package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicBecomeTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.action.MagicPermanentAction;

public class Aven_Mimeomancer {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			return (permanent.getController()==data) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                            "You may put a feather counter on target creature.",
                            MagicTargetChoice.TARGET_CREATURE),
                        new MagicBecomeTargetPicker(3,1,true),
                        MagicEvent.NO_DATA,
                        this,
                        "You may$ put a feather counter on target creature$."):
                null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.Feather,1,true));
                    }
                });
            }
		}
    };
}
