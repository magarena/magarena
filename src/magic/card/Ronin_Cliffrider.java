package magic.card;

import java.util.Collection;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicBecomesBlockedPumpTrigger;
import magic.model.trigger.MagicWhenAttacksTrigger;
import magic.model.trigger.MagicWhenBlocksPumpTrigger;

public class Ronin_Cliffrider {
	private static final int amount = 1;
	
	public static final MagicBecomesBlockedPumpTrigger T1 = new MagicBecomesBlockedPumpTrigger(amount,amount);
	
	public static final MagicWhenBlocksPumpTrigger T2 = new MagicWhenBlocksPumpTrigger(amount,amount);
	
	public static final MagicWhenAttacksTrigger T3 = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPlayer player = creature.getController();
			return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                        		player + " may$ have " + permanent + " deal 1 damage " +
                                "to each creature defending player controls."),
                        new Object[]{permanent,game.getOpponent(player)},
                        this,
                        player + " may$ have " + permanent + " deal 1 damage " +
                        "to each creature defending player controls.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicSource source = (MagicSource)data[0];
				final MagicPlayer defendingPlayer = (MagicPlayer)data[1];
				final Collection<MagicTarget> creatures =
						game.filterTargets(defendingPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
				for (final MagicTarget creature : creatures) {
					final MagicDamage damage = new MagicDamage(source,creature,1,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };
}
