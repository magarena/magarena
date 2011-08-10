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

public class Kazuul__Tyrant_of_the_Cliffs {

    public static final MagicTrigger V7698 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Kazuul, Tyrant of the Cliffs") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent creature=(MagicPermanent)data;
			if (creature.getController()!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,
					"Put a 3/3 red Ogre creature token onto the battlefield unless your opponent pays {3}.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[1];
			game.addEvent(new MagicPlayOgreUnlessEvent((MagicPermanent)data[0],game.getOpponent(player),player,MagicManaCost.THREE));
		}
    };
    
}
