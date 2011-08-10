package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTapTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Tumble_Magnet {

    public static final MagicPermanentActivation V2730 =new MagicPermanentActivation(            "Tumble Magnet",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Tap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_CREATURE,
                    new MagicTapTargetPicker(true,false),
                    MagicEvent.NO_DATA,
                    this,
                    "Tap target artifact or creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=(MagicPermanent)event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};

    public static final MagicTrigger V10325 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Tumble Magnet") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{permanent},this,
					"Tumble Magnet enters the battlefield with three charge counters on it.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,3,false));
		}

		@Override
		public boolean usesStack() {
			return false;
		}
    };
    
}
