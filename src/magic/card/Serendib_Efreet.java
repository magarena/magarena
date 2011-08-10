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

public class Serendib_Efreet {

    public static final MagicTrigger V8699 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Serendib Efreet") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Serendib Efreet deals 1 damage to you.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],1,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
    
}
