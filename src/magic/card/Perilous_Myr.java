package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Perilous_Myr {

    public static final MagicTrigger V8419 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Perilous Myr") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(2),
					new Object[]{permanent},this,"Perilous Myr deals 2 damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
