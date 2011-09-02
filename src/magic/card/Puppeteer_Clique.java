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


public class Puppeteer_Clique {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			
			return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{player},this,
                    "Put target creature card$ in an opponent's graveyard onto the battlefield under your control. "+
                    "It has haste. At the end of your turn, exile it.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction(
                                (MagicPlayer)data[0],
                                card,
                                MagicPlayCardAction.HASTE_REMOVE_AT_END_OF_YOUR_TURN));
                }
			});
		}
    };
}
