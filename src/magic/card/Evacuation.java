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

public class Evacuation {

	public static final MagicSpellCardEvent V3753 =new MagicSpellCardEvent("Evacuation") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),new Object[]{cardOnStack},this,
				"Return all creatures to their owner's hand.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));			
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)target,MagicLocationType.OwnersHand));
			}
		}
	};
	
}
