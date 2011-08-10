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

public class Archon_of_Redemption {

    public static final MagicTrigger V6745 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Archon of Redemption") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player,permanent},this,"You gain life equal to Archon of Redemption's power.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[1];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower(game)));
		}		
    };

    public static final MagicTrigger V6762 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Archon of Redemption") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (otherPermanent!=permanent&&otherPermanent.getController()==player&&otherPermanent.isCreature()&&
				otherPermanent.hasAbility(game,MagicAbility.Flying)) {
				return new MagicEvent(permanent,player,new Object[]{player,otherPermanent},this,
					"You gain life equal to the power of "+otherPermanent.getName()+'.');
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[1];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower(game)));
		}		
    };
    
}
