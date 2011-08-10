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

public class Szadek__Lord_of_Secrets {

    public static final MagicTrigger V9090 =new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Szadek, Lord of Secrets",6) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getAmount();
			if (amount>0&&damage.isCombat()&&permanent==damage.getSource()&&damage.getTarget().isPlayer()) {
				// Replacement effect.
				damage.setAmount(0);
				game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount,true));
				game.doAction(new MagicMillLibraryAction((MagicPlayer)damage.getTarget(),amount));
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
    
}
