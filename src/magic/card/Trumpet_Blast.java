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

public class Trumpet_Blast {

	public static final MagicSpellCardEvent V4738 =new MagicSpellCardEvent("Trumpet Blast") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack},this,"Attacking creatures get +2/+0 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];			
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,2,0));
			}
		}
	};

}
