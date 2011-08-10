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

public class Stonebrow__Krosan_Hero {

    public static final MagicTrigger V9052 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Stonebrow, Krosan Hero") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent creature=(MagicPermanent)data;
			if (creature.getController()==player&&creature.hasAbility(game,MagicAbility.Trample)) {
				return new MagicEvent(permanent,player,new Object[]{creature},this,creature.getName()+" gets +2/+2 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}
    };
    
}
