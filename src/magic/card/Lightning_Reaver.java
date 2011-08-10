package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Lightning_Reaver {

    public static final MagicTrigger V7822 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Lightning Reaver") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Put a charge counter on Lightning Reaver.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
    };

    public static final MagicTrigger V7841 =new MagicTrigger(MagicTriggerType.AtEndOfTurn,"Lightning Reaver") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				final int counters=permanent.getCounters(MagicCounterType.Charge);
				if (counters>0) {
					return new MagicEvent(permanent,player,new Object[]{permanent,game.getOpponent(player)},this,
						"Lightning Reaver deals damage equal to the number of charge counters on it to your opponent.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final int counters=permanent.getCounters(MagicCounterType.Charge);
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],counters,false);
			game.doAction(new MagicDealDamageAction(damage));
		}		
    };
    
}
