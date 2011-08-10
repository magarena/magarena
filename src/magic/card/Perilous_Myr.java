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
