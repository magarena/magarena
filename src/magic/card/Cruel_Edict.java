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

public class Cruel_Edict {

	public static final MagicSpellCardEvent V5380 =new MagicSpellCardEvent("Cruel Edict") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_OPPONENT,new Object[]{cardOnStack},this,
				"Target opponent$ sacrifices a creature.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer opponent=(MagicPlayer)event.getTarget(game,choiceResults,0);
			if (opponent!=null&&opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent(cardOnStack.getCard(),opponent,MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
	};
	
}
