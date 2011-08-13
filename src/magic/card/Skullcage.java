package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Skullcage {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,data},
                        this,
                        "Skullcage deals 2 damage to your opponent " + 
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
