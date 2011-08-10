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

public class Murderous_Redcap {

    public static final MagicTrigger V8159 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Murderous Redcap") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final int power=permanent.getPower(game);
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(power),
				new Object[]{permanent},this,"Murderous Redcap deals damage equal to its power to target creature or player$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(permanent,target,permanent.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
