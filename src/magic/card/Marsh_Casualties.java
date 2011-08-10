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

public class Marsh_Casualties {

	public static final MagicSpellCardEvent V5653 =new MagicSpellCardEvent("Marsh Casualties") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_PLAYER,MagicManaCost.THREE,false),
				new Object[]{cardOnStack},this,
				"Creatures target player$ controls get -1/-1 until end of turn. "+
				"If Marsh Casualties was kicked$, those creatures get -2/-2 until end of turn instead.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=(MagicPlayer)choiceResults[0];
			final int amount=(Integer)choiceResults[1]>0?-2:-1;
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,amount,amount));
			}
		}
	};
	
}
