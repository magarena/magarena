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

public class Vigor {

    public static final MagicTrigger V9345 =new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Vigor",4) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			final int amount=damage.getAmount();
			if (!damage.isUnpreventable()&&amount>0&&target!=permanent&&target.isPermanent()&&target.getController()==player) {
				final MagicPermanent creature=(MagicPermanent)target;
				if (creature.isCreature()) {
					// Prevention effect.
					damage.setAmount(0);
					return new MagicEvent(permanent,player,new Object[]{creature,amount},this,"Put "+amount+" +1/+1 counters on "+creature.getName()+".");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,(Integer)data[1],true));
		}
    };
    
    public static final MagicTrigger V9372 =new MagicFromGraveyardToLibraryTrigger("Vigor");

}
