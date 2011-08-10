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

public class Sunblast_Angel {

	public static final MagicTrigger V8852 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Sunblast Angel") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"Destroy all tapped creatures.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_TAPPED_CREATURE);
			for (final MagicTarget target : targets) {

				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
    };
    
}
