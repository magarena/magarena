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

public class Swords_to_Plowshares {

	public static final MagicSpellCardEvent V4619 =new MagicSpellCardEvent("Swords to Plowshares") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				MagicTargetChoice.NEG_TARGET_CREATURE,MagicExileTargetPicker.getInstance(),new Object[]{cardOnStack},this,
				"Exile target creature$. Its controller gains life equal to its power.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeLifeAction(creature.getController(),creature.getPower(game)));
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
			}
		}
	};
	
}
