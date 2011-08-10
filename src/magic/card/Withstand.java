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

public class Withstand {

	public static final MagicSpellCardEvent V5014 =new MagicSpellCardEvent("Withstand") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE_OR_PLAYER,MagicPreventTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Prevent the next 3 damage that would be dealt to target creature or player$ this turn. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				game.doAction(new MagicPreventDamageAction(target,3));
			}
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
	};

}
