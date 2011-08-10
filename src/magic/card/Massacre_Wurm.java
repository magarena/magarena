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

public class Massacre_Wurm {

	public static final MagicTrigger V7979 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Massacre Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{game.getOpponent(player)},this,
				"Creatures your opponent controls get -2/-2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,-2,-2));
			}
		}
    };

    public static final MagicTrigger V8000 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Massacre Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer otherController=otherPermanent.getController();
			if (otherController!=player&&otherPermanent.isCreature()) {			
				return new MagicEvent(permanent,player,new Object[]{otherController},this,"Your opponent loses 2 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-2));
		}
    };

}
