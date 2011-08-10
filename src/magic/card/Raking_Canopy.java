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

public class Raking_Canopy {

    public static final MagicTrigger V10252 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Raking Canopy") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent creature=(MagicPermanent)data;
			if (creature.getController()!=permanent.getController()&&creature.hasAbility(game,MagicAbility.Flying)) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,creature},this,
					"Raking Canopy deals 4 damage to "+creature.getName()+".");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],4,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };

}
