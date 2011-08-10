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

public class Smother {

	public static final MagicSpellCardEvent V4597 =new MagicSpellCardEvent("Smother") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				MagicTargetChoice.NEG_TARGET_CREATURE_CONVERTED_3_OR_LESS,new MagicDestroyTargetPicker(true),
				new Object[]{cardOnStack},this,"Destroy target creature$ with converted mana cost 3 or less. It can't be regenerated.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(creature));
			}
		}
	};
	
}
