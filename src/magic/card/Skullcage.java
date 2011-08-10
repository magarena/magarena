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

public class Skullcage {

    public static final MagicTrigger V10428 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Skullcage") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(permanent,player,new Object[]{permanent,data},this,
					"Skullcage deals 2 damage to your opponent unless your opponent has exactly three or exactly four cards in hand.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer opponent=(MagicPlayer)data[1];
			final int amount=opponent.getHandSize();
			if (amount<3||amount>4) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],opponent,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
}
