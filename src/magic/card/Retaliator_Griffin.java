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

public class Retaliator_Griffin {

    public static final MagicTrigger V8583 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Retaliator Griffin") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getDealtAmount();
			final MagicPlayer player=permanent.getController();
			if (amount>0&&damage.getTarget()==player&&damage.getSource().getController()!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent,amount},this,"Put "+amount+" +1/+1 counters on Retaliator Griffin.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,(Integer)data[1],true));
		}		
    };

}
