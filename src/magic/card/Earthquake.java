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

public class Earthquake {

	public static final MagicSpellCardEvent V5486 =new MagicSpellCardEvent("Earthquake") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,amount},this,
				"Earthquake deals "+amount+" damage to each creature without flying and each player.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicSource source=cardOnStack.getCard();
			final int amount=(Integer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget target : targets) {
			
				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			for (final MagicPlayer player : game.getPlayers()) {
				
				final MagicDamage damage=new MagicDamage(source,player,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

}
