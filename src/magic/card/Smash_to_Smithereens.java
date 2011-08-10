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

public class Smash_to_Smithereens {

	public static final MagicSpellCardEvent V4553 =new MagicSpellCardEvent("Smash to Smithereens") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_ARTIFACT,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack},this,
				"Destroy target artifact$. Smash to Smithereens deals 3 damage to that artifact's controller.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),permanent.getController(),3,false);
				game.doAction(new MagicDestroyAction(permanent));
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
