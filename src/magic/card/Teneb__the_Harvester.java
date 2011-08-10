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

public class Teneb__the_Harvester {

    public static final MagicTrigger V9132 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Teneb, the Harvester") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {2}{B}.",new MagicPayManaCostChoice(MagicManaCost.TWO_BLACK),MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS),
    new Object[]{player},this,
					"You may$ pay {2}{B}$. If you do, put target creature card$ in a graveyard into play under your control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,2);
				if (card!=null) {
					game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
				}				
			}
		}
    };
    
}
