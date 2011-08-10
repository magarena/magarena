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

public class Twincast {

	public static final MagicSpellCardEvent V4804 =new MagicSpellCardEvent("Twincast") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
				new Object[]{cardOnStack,player},this,"Copy target instant or sorcery spell$. You may choose new targets for the copy.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCopyCardOnStackAction((MagicPlayer)data[1],targetSpell));
			}
		}
	};
	
}
