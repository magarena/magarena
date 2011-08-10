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

public class Thoughtweft_Gambit {

	public static final MagicSpellCardEvent V4664 =new MagicSpellCardEvent("Thoughtweft Gambit") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Tap all creatures your opponent controls and untap all creatures you control.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			
			final MagicPlayer player=(MagicPlayer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {

				final MagicPermanent creature=(MagicPermanent)target;
				if (creature.getController()==player) {
					game.doAction(new MagicUntapAction(creature));
				} else {
					game.doAction(new MagicTapAction(creature,true));
				}
			}
		}
	};
	
}
