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

public class Time_Warp {

	public static final MagicSpellCardEvent V6139 =new MagicSpellCardEvent("Time Warp") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_PLAYER,					
				new Object[]{cardOnStack},this,"Target player$ takes an extra turn after this one.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeExtraTurnsAction(player,1));
			}
		}
	};
	
}
