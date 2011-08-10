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

public class Captain_of_the_Watch {

    public static final MagicTrigger V6941 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Captain of the Watch") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put three 1/1 white Soldier creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SOLDIER_TOKEN_CARD));
			}
		}		
    };

}
