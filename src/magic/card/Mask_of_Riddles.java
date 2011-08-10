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

public class Mask_of_Riddles {

    public static final MagicTrigger V9584 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Mask of Riddles") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (permanent.getEquippedCreature()==damage.getSource()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				return new MagicDrawEvent(permanent,permanent.getController(),1);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
        
}
