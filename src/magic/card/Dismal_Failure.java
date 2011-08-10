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

public class Dismal_Failure {

	public static final MagicSpellCardEvent V3644 =new MagicSpellCardEvent("Dismal Failure") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack},this,"Counter target spell$. Its controller discards a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicCardOnStack counteredCard=event.getTarget(game,choiceResults,0);
			if (counteredCard!=null) {
				game.doAction(new MagicCounterItemOnStackAction(counteredCard));
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),counteredCard.getController(),1,false));
			}
		}
	};
		
}
