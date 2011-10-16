package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Rapacious_One {
	public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final int amount = damage.getDealtAmount();
			final MagicPlayer player = permanent.getController();
			return (amount > 0 &&
					damage.getSource() == permanent &&
					damage.getTarget().isPlayer() &&
					damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,amount},
                        this,
                        amount > 1 ?
                        		"Put " + amount + " 0/1 colorless Eldrazi Spawn " +
                        		"creature tokens onto the battlefield. They have " +
                        		"\"Sacrifice this creature: Add {1} to your mana pool.\"" :
                        		"Put a 0/1 colorless Eldrazi Spawn " +
                            	"creature token onto the battlefield. It has " +
                            	"\"Sacrifice this creature: Add {1} to your mana pool.\"") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			for (int count=(Integer)data[1];count>0;count--) {
				game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.getInstance().getTokenDefinition("Eldrazi Spawn")));
			}
			
		}		
    };
}
