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

public class Recoil {

	public static final MagicSpellCardEvent V4418 =new MagicSpellCardEvent("Recoil") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_PERMANENT,
				MagicBounceTargetPicker.getInstance(),new Object[]{cardOnStack},this,
				"Return target permanent$ to its owner's hand. Then that player discards a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				final MagicPlayer owner=permanent.getCard().getOwner();
				game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),owner,1,false));
			}
		}
	};
	
}
