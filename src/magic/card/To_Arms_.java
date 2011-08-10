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

public class To_Arms_ {

	public static final MagicSpellCardEvent V4715 =new MagicSpellCardEvent("To Arms!") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,"Untap all creatures you control. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=(MagicPlayer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicUntapAction((MagicPermanent)target));
			}
			game.doAction(new MagicDrawAction(player,1));
		}
	};

}
