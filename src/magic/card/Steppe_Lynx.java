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

public class Steppe_Lynx {

    public static final MagicTrigger V8809 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Steppe Lynx") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent played=(MagicPermanent)data;
			if (player==played.getController()&&played.isLand()) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Steppe Lynx gets +2/+2 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}		
    };
    
}
