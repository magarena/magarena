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

public class Flametongue_Kavu {

    public static final MagicTrigger V7344 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Flametongue Kavu") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,new MagicDamageTargetPicker(4),
				new Object[]{permanent},this,"Flametongue Kavu deals 4 damage to target creature$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,4,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };

}
