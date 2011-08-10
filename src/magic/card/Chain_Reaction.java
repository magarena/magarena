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

public class Chain_Reaction {

	public static final MagicSpellCardEvent V5328 =new MagicSpellCardEvent("Chain Reaction") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack},this,
				"Chain Reaction deals X damage to each creature, where X is the number of creatures on the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=game.getNrOfPermanents(MagicType.Creature);
			final MagicSource source=cardOnStack.getCard();
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {

				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

}
