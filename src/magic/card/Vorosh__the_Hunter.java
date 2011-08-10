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

public class Vorosh__the_Hunter {

    public static final MagicTrigger V9374 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Vorosh, the Hunter") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice("You may pay {2}{G}.",new MagicPayManaCostChoice(MagicManaCost.TWO_GREEN)),
    new Object[]{permanent},this,
					"You may$ pay {2}{G}$. If you do, put six +1/+1 counters on Vorosh.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,6,true));
			}
		}
    };

}
