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

public class Hideous_End {

	public static final MagicSpellCardEvent V3940 =new MagicSpellCardEvent("Hideous End") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack},this,"Destroy target nonblack creature$. Its controller loses 2 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer controller=creature.getController();
				game.doAction(new MagicDestroyAction(creature));
				game.doAction(new MagicChangeLifeAction(controller,-2));
			}
		}
	};

}
