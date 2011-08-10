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

public class Grave_Titan {

    public static final MagicTrigger V7540 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Grave Titan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 2/2 black Zombie creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
		}		
    };

    public static final MagicTrigger V7558 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Grave Titan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 2/2 black Zombie creature tokens onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
		}		
    };
    
}
