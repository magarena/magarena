package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Quest_for_the_Gravelord {

	public static final MagicPermanentActivation V2427 =new MagicPermanentActivation(            "Quest for the Gravelord",
			new MagicCondition[]{MagicCondition.THREE_CHARGE_COUNTERS_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.Charge,3),
				new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put a 5/5 black Zombie Giant creature token onto the battlefield.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.ZOMBIE_GIANT_TOKEN_CARD));
		}
	};
	
    public static final MagicTrigger V10233 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Quest for the Gravelord") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent.isCreature()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Put a quest counter on Quest for the Gravelord.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
    };
        
}
