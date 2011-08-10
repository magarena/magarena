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

public class Rumbling_Slum {

    public static final MagicTrigger V8897 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Rumbling Slum") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=(MagicPlayer)data;
			if (permanent.getController()==player) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Rumbling Slum deals 1 damage to each player.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
			for (final MagicPlayer player : game.getPlayers()) {
			
				final MagicDamage damage=new MagicDamage(source,player,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
