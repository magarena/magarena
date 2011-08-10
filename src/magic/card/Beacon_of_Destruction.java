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

public class Beacon_of_Destruction {

	public static final MagicSpellCardEvent V3388 =new MagicSpellCardEvent("Beacon of Destruction") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(card,player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(5),new Object[]{card},this,
				"Beacon of Destruction deals 5 damage to target creature or player$. Shuffle Beacon of Destruction into its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCard card=(MagicCard)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(card,target,5,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicShuffleIntoLibraryAction(card));
		}
	};
	
}
