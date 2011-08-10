package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Shrine_of_Burning_Rage {

	public static final MagicPermanentActivation V2806 =new MagicPermanentActivation(            "Shrine of Burning Rage",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.THREE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE),
				new MagicSacrificeEvent(permanent)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(source.getCounters(MagicCounterType.Charge)),
                    new Object[]{source},
                    this,
                    "Shrine of Burning Rage deals damage equal to the number of charge counters on it to target creature or player$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicPermanent source=(MagicPermanent)data[0];
				final int amount=source.getCounters(MagicCounterType.Charge);
				if (amount>0) {
					final MagicDamage damage=new MagicDamage(source,target,amount,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
	};
	
    public static final MagicTrigger V10389 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Shrine of Burning Rage") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a charge counter on Shrine of Burning Rage.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}		
    };
    
    public static final MagicTrigger V10408 =new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Shrine of Burning Rage") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.Red.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a charge counter on Shrine of Burning Rage.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}		
    };
    
}
