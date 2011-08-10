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

public class Promise_of_Bunrei {

    public static final MagicTrigger V10180 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Promise of Bunrei") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent.isCreature()&&otherPermanent.getController()==player) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,
					"Sacrifice Promise of Bunrei. If you do, put four 1/1 colorless Spirit creature tokens onto the battlefield.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPlayer player=(MagicPlayer)data[1];
			if (player.controlsPermanent(permanent)) {
				game.doAction(new MagicSacrificeAction(permanent));
			
				for (int count=4;count>0;count--) {
				
					game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SPIRIT_TOKEN_CARD));
				}
			}
		}
    };
    
}
