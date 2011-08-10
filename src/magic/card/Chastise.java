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

public class Chastise {

	public static final MagicSpellCardEvent V3465 =new MagicSpellCardEvent("Chastise") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack,player},this,"Destroy target attacking creature$. You gain life equal to its power.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final int power=creature.getPower(game);
				game.doAction(new MagicDestroyAction(creature));
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],power));
			}
		}
	};

}
