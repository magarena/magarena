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

public class Mana_Leak {

	public static final MagicSpellCardEvent V4156 =new MagicSpellCardEvent("Mana Leak") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack},this,"Counter target spell$ unless its controller pays {3}.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.addEvent(new MagicCounterUnlessEvent(cardOnStack.getCard(),targetSpell,MagicManaCost.THREE));
			}
		}
	};
	
}
