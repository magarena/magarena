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

public class Wrexial__the_Risen_Deep {

    public static final MagicTrigger V9473 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Wrexial, the Risen Deep") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may cast target instant or sorcery card from your opponent's graveyard.",
			MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),new Object[]{player},this,
					"You may$ cast target instant or sorcery card$ from your opponent's graveyard without paying its mana cost. "+
					"If that card would be put into a graveyard this turn, exile it instead.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,1);
				if (card!=null) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,(MagicPlayer)data[0],MagicPayedCost.NO_COST);
					cardOnStack.setMoveLocation(MagicLocationType.Exile);
					game.doAction(new MagicPutItemOnStackAction(cardOnStack));
				}
			}
		}
    };

}
