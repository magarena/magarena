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

public class Undermine {

	public static final MagicSpellCardEvent V4825 =new MagicSpellCardEvent("Undermine") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack},this,"Counter target spell$. Its controller loses 3 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack counteredCard=event.getTarget(game,choiceResults,0);
			if (counteredCard!=null) {
				game.doAction(new MagicCounterItemOnStackAction(counteredCard));
				game.doAction(new MagicChangeLifeAction(counteredCard.getController(),-3));
			}
		}
	};
	
}
