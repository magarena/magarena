package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Skinrender {

    public static final MagicTrigger V8921 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Skinrender") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,new MagicWeakenTargetPicker(-3,-3),
				MagicEvent.NO_DATA,this,"Put three -1/-1 counters on target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.MinusOne,3,true));
			}
		}
    };
    
}
