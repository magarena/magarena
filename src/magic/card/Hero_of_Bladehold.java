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

public class Hero_of_Bladehold {

    public static final MagicTrigger V7605 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Hero of Bladehold") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put two 1/1 white Soldier creature tokens onto the battlefield tapped and attacking.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=2;count>0;count--) {
			
				final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.SOLDIER_TOKEN_CARD,player);
				game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.TAPPED_ATTACKING));
			}
		}		
    };
    
}
