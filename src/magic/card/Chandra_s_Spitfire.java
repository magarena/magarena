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

public class Chandra_s_Spitfire {

    public static final MagicTrigger V6983 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Chandra's Spitfire") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			if (!damage.isCombat()&&target.isPlayer()&&target!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Chandra's Spitfire gets +3/+0 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],3,0));
		}
    };
    
}
