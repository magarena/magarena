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

public class Midnight_Banshee {

    public static final MagicTrigger V8059 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Midnight Banshee") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"Put a -1/-1 counter on each nonblack creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_NONBLACK_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeCountersAction((MagicPermanent)target,MagicCounterType.MinusOne,1,true));			
			}
		}
    };

}
