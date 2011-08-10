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

public class Black_Sun_s_Zenith {

	public static final MagicSpellCardEvent V5220 =new MagicSpellCardEvent("Black Sun's Zenith") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {

			final MagicPlayer player=cardOnStack.getController();
			final MagicCard card=cardOnStack.getCard();
			final int amount=payedCost.getX();
			return new MagicEvent(card,player,new Object[]{card,player,amount},this,
				"Put "+amount+" -1/-1 counters on each creature. Shuffle Black Sun's Zenith into its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final int amount=(Integer)data[2];
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
			
				game.doAction(new MagicChangeCountersAction((MagicPermanent)target,MagicCounterType.MinusOne,amount,true));
			}
			game.doAction(new MagicShuffleIntoLibraryAction((MagicCard)data[0]));
		}
	};
	
}
