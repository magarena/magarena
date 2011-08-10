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

public class Zealous_Persecution {

	public static final MagicSpellCardEvent V5101 =new MagicSpellCardEvent("Zealous Persecution") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Until end of turn, creatures you control get +1/+1 and creatures your opponent controls get -1/-1.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=(MagicPlayer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				if (creature.getController()==player) {
					game.doAction(new MagicChangeTurnPTAction(creature,1,1));
				} else {
					game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
				}
			}
		}
	};
	
	// ***** SORCERIES *****
	
}
