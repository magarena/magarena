package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Skullcage {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,data},
                        this,
                        permanent + " deals 2 damage to your opponent " + 
                        "unless your opponent has exactly three or exactly four cards in hand.");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer opponent=(MagicPlayer)data[1];
			final int amount=opponent.getHandSize();
			if (amount<3||amount>4) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],opponent,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
}
