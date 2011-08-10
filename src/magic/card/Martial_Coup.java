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

public class Martial_Coup {

	public static final MagicSpellCardEvent V5681 =new MagicSpellCardEvent("Martial Coup") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int x=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player,x},this,
				"Put "+x+" 1/1 white Soldier creature tokens onto the battlefield."+(x>=5?" Destroy all other creatures.":""));
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=(MagicPlayer)data[1];
			int x=(Integer)data[2];
			final Collection<MagicTarget> targets;
			if (x>=5) {
				targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
			} else {
				targets=Collections.<MagicTarget>emptyList();
			}
			for (;x>0;x--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SOLDIER_TOKEN_CARD));
			}
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
	};

}
