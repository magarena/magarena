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

public class Stupor {

	public static final MagicSpellCardEvent V6096 =new MagicSpellCardEvent("Stupor") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_OPPONENT,
				new Object[]{cardOnStack},this,"Target opponent$ discards a card at random, then discards a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,1,true));
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,1,false));
			}
		}
	};

}
