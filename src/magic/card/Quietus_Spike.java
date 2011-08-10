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

public class Quietus_Spike {

    public static final MagicTrigger V9602 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Quietus Spike") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			if (permanent.getEquippedCreature()==damage.getSource()&&target.isPlayer()&&damage.isCombat()) {
				return new MagicEvent(permanent,(MagicPlayer)target,new Object[]{target},this,"You lose half your life, rounded up.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(player,-(player.getLife()+1)/2));
		}
    };
    
}
