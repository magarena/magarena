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

public class Exhaustion {

	public static final MagicSpellCardEvent V5518 =new MagicSpellCardEvent("Exhaustion") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_OPPONENT,
				new Object[]{cardOnStack},this,"Creatures and lands target opponent$ controls don't untap during that opponent's next untap step.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangePlayerStateAction(player,MagicPlayerState.Exhausted,true));
			}
		}
	};
	
}
