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

public class Rite_of_Replication {

	public static final MagicSpellCardEvent V5860 =new MagicSpellCardEvent("Rite of Replication") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(MagicTargetChoice.TARGET_CREATURE,MagicManaCost.FIVE,false),
				MagicCopyTargetPicker.getInstance(),new Object[]{cardOnStack,player},this,
				"Put a token onto the battlefield that's a copy of target creature$. "+
				"If Rite of Replication was kicked$, put five of those tokens onto the battlefield instead.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer player=(MagicPlayer)data[1];
				final MagicCardDefinition cardDefinition=creature.getCardDefinition();
				int count=(Integer)choiceResults[1]>0?5:1;
				for (;count>0;count--) {

					game.doAction(new MagicPlayTokenAction(player,cardDefinition));
				}
			}
		}
	};
	
}
