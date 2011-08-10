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

public class Lavaborn_Muse {

    public static final MagicTrigger V7801 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Lavaborn Muse") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=(MagicPlayer)data;
			if (permanent.getController()!=player&&player.getHandSize()<3) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,player},this,
					"Lavaborn Muse deals 3 damage to your opponent.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],3,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };

}
