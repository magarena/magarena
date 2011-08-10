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

public class Into_the_Roil {

	public static final MagicSpellCardEvent V4009 =new MagicSpellCardEvent("Into the Roil") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				new MagicKickerChoice(MagicTargetChoice.TARGET_NONLAND_PERMANENT,MagicManaCost.ONE_BLUE,false),MagicBounceTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Return target nonland permanent$ to its owner's hand. If Into the Roil was kicked$, draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
				if (((Integer)choiceResults[1])>0) {
					game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
				}
			}
		}
	};
	
}
