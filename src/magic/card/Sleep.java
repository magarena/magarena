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

public class Sleep {

	public static final MagicSpellCardEvent V5983 =new MagicSpellCardEvent("Sleep") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_PLAYER,new Object[]{cardOnStack},this,
				"Tap all creatures target player$ controls. Those creatures don't untap during their controller's next untap step.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
				for (final MagicTarget target : targets) {
					
					final MagicPermanent creature=(MagicPermanent)target;
					game.doAction(new MagicTapAction(creature,true));
					game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.DoesNotUntap,true));
				}
			}
		}
	};

}
