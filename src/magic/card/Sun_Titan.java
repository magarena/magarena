package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Sun_Titan {
    public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
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
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
                }
			});
		}
    };
    
    public static final MagicWhenAttacksTrigger T2 = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			if (permanent==data) {
			    final MagicPlayer player = permanent.getController(); 
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
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
                }
			});
		}
    };
}
