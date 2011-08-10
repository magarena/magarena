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

public class Rally_the_Forces {

	public static final MagicSpellCardEvent V4393 =new MagicSpellCardEvent("Rally the Forces") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack},this,
				"Attacking creatures get +1/+0 and gain first strike until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];			
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {

				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
			}
		}
	};
	
}
