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

public class Offering_to_Asha {

	public static final MagicSpellCardEvent V4259 =new MagicSpellCardEvent("Offering to Asha") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack,player},this,"Counter target spell$ unless its controller pays {4}. You gain 4 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],4));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.addEvent(new MagicCounterUnlessEvent(cardOnStack.getCard(),targetSpell,MagicManaCost.FOUR));
			}
		}
	};

}
