package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Tuktuk_Scrapper {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally,game)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                        		player + " may destroy target artifact.",
    							MagicTargetChoice.NEG_TARGET_ARTIFACT),
    					new MagicDestroyTargetPicker(false),
                        new Object[]{player,permanent},
                        this,
                        player + " may$ destroy target artifact$.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
	                public void doAction(final MagicPermanent target) {
	                    game.doAction(new MagicDestroyAction(target));
	                    final MagicCard card = target.getCard();
	                    final MagicPlayer player = (MagicPlayer)data[0];
	                    // only deal damage when the target is destroyed
	                    if (card.getOwner().getGraveyard().contains(card)
	                    	||
	                    	(card.isToken() &&
	                    	!card.getOwner().getPermanents().contains(target))) {
	                    	final int amount =
		                			player.getNrOfPermanentsWithSubType(MagicSubType.Ally,game);
		                    if (amount > 0) {
		                    	final MagicDamage damage = new MagicDamage(
		                    			(MagicPermanent)data[1],
		                    			card.getOwner(),
		                    			amount,
		                    			false);
		                    	game.doAction(new MagicDealDamageAction(damage));
		                    }
	                    }
	                }
	            });
			}			
		}		
    };
}
