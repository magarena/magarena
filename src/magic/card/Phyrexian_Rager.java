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

public class Phyrexian_Rager {

    public static final MagicTrigger V8463 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Phyrexian Rager") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You draw a card and you lose 1 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,1));
			game.doAction(new MagicChangeLifeAction(player,-1));
		}
    };
    
}
