package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

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
