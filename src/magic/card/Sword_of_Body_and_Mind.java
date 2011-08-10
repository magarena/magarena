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

public class Sword_of_Body_and_Mind {

    public static final MagicTrigger V9710 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of Body and Mind") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicTarget targetPlayer=damage.getTarget();
				return new MagicEvent(permanent,player,new Object[]{player,targetPlayer},this,
					"You put a 2/2 green Wolf creature token onto the battlefield and "+targetPlayer.getName()+
					" puts the top ten cards of his or her library into his or her graveyard.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.WOLF_TOKEN_CARD));
			game.doAction(new MagicMillLibraryAction((MagicPlayer)data[1],10));
		}
    };
    
}
