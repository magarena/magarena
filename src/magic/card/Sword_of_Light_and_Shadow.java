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

public class Sword_of_Light_and_Shadow {

    public static final MagicTrigger V9793 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of Light and Shadow") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may return target creature card from your graveyard to your hand.",
            MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),new Object[]{player},this,
					"You gain 3 life and you may$ return target creature card$ from your graveyard to your hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,1);
				if (card!=null) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard)); 
					game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
				}
			}
		}
    };
    
}
