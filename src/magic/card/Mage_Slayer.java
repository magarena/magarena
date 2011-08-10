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

public class Mage_Slayer {

    public static final MagicTrigger V9533 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Mage Slayer",1) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
			if (equippedCreature!=null&&equippedCreature==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{equippedCreature,game.getOpponent(player)},this,
						equippedCreature.getName()+" deals damage equal to its power to defending player.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],permanent.getPower(game),false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
    
}
