package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;

public class Sun_Titan {
    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
            return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{player},
                    this,
                    "Return target permanent card$ with converted mana cost 3 or less " + 
                    "from your graveyard to the battlefield.");
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCard card=event.getTarget(game,choiceResults,0);
			if (card != null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
			}
		}
    };
    
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			if (permanent==data) {
			    final MagicPlayer player=permanent.getController();
                return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{player},
                    this,
                    "Return target permanent card$ with converted mana cost 3 or less " + 
                    "from your graveyard to the battlefield.");
            }
            return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCard card=event.getTarget(game,choiceResults,0);
			if (card != null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
			}
		}
    };
}
