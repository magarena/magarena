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

public class Volcanic_Fallout {

	public static final MagicSpellCardEvent V4962 =new MagicSpellCardEvent("Volcanic Fallout") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),new Object[]{cardOnStack},this,
				"Volcanic Fallout deals 2 damage to each creature and each player.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));			
			final MagicSource source=cardOnStack.getCard();
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {

				final MagicDamage damage=new MagicDamage(source,target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			for (final MagicPlayer player : game.getPlayers()) {
				
				final MagicDamage damage=new MagicDamage(source,player,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
