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

public class Shield_of_the_Righteous {

    public static final MagicTrigger V9645 =new MagicTrigger(MagicTriggerType.WhenBlocks,"Shield of the Righteous") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
			if (equippedCreature==data&&equippedCreature!=null) {
				final MagicPermanent blocked=equippedCreature.getBlockedCreature();
				if (blocked!=null) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{blocked},this,
							blocked.getName()+" doesn't untap during its controller's next untap step.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DoesNotUntap,true));
		}
    };
    
}
