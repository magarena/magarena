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

public class Debtors__Knell {

    public static final MagicTrigger V10025 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Debtors' Knell") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,MagicGraveyardTargetPicker.getInstance(),
					new Object[]{player},this,"Put target creature card$ in a graveyard onto the battlefield under your control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCard card=event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
			}
		}
    };
    
}
