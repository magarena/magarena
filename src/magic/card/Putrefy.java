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

public class Putrefy {

	public static final MagicSpellCardEvent V4371 =new MagicSpellCardEvent("Putrefy") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_ARTIFACT,new MagicDestroyTargetPicker(true),
				new Object[]{cardOnStack},this,"Destroy target artifact or creature$. It can't be regenerated.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};

}
