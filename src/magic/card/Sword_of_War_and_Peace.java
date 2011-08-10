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

public class Sword_of_War_and_Peace {

    public static final MagicTrigger V9824 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of War and Peace") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicTarget targetPlayer=damage.getTarget();
				return new MagicEvent(permanent,player,new Object[]{permanent,player,targetPlayer},this,
					"Sword of War and Peace deals damage to "+targetPlayer.getName()+" equal to the number of cards in his or her hand and "+
					"you gain 1 life for each card in your hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer targetPlayer=(MagicPlayer)data[2];
			final int amount1=targetPlayer.getHand().size();
			if (amount1>0) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],targetPlayer,amount1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			final MagicPlayer player=(MagicPlayer)data[1];
			final int amount2=player.getHand().size();
			if (amount2>0) {
				game.doAction(new MagicChangeLifeAction(player,amount2));
			}
		}
    };
    
}
