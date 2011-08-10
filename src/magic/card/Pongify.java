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

public class Pongify {

	public static final MagicSpellCardEvent V4282 =new MagicSpellCardEvent("Pongify") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_CREATURE,
				new MagicDestroyTargetPicker(true),new Object[]{cardOnStack},this,
				"Destroy target creature$. It can't be regenerated. That creature's controller puts a 3/3 green Ape creature onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer controller=creature.getController();
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(creature));
				game.doAction(new MagicPlayTokenAction(controller,TokenCardDefinitions.APE_TOKEN_CARD));
			}
		}
	};
	
}
