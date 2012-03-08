package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Antagonism {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
    	@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
    		final MagicPlayer opponent = game.getOpponent(player);
    		return (!opponent.hasState(MagicPlayerState.WasDealtDamage)) ?
    			new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent,player},
                    this,
                    permanent + " deals 2 damage to " + player + "."):
                MagicEvent.NONE;
    	}
    	
    	@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicDamage damage = new MagicDamage(
					(MagicPermanent)data[0],
					(MagicPlayer)data[1],
					2,
					false);
            game.doAction(new MagicDealDamageAction(damage));
		}	
    };
}
