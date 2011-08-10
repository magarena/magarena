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

public class Corpsehatch {

	public static final MagicSpellCardEvent V5354 =new MagicSpellCardEvent("Corpsehatch") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack,player},this,
				"Destroy target nonblack creature$. Put two 0/1 colorless Eldrazi Spawn creature tokens onto the battlefield. "+
				"They have \"Sacrifice this creature: Add {1} to your mana pool.\"");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicDestroyAction(creature));
			}
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ELDRAZI_SPAWN_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ELDRAZI_SPAWN_TOKEN_CARD));
		}
	};
	
}
