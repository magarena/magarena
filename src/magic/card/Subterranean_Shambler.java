package magic.card;

import java.util.Collection;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Subterranean_Shambler {
	public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,player},
                    this,
                    permanent + " deals 1 damage to each creature without flying.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicSource source = (MagicSource)data[0];
			final Collection<MagicTarget> creatures =
                game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget creature : creatures) {
				final MagicDamage damage = new MagicDamage(source,creature,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    public static final MagicWhenLeavesPlayTrigger T3 = new MagicWhenLeavesPlayTrigger() {
    	@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPermanent data) {
			return (permanent == data) ?
				new MagicEvent(
						permanent,
						permanent.getController(),
	                    new Object[]{permanent,permanent.getController()},
	                    this,
	                    permanent + " deals 1 damage to each creature without flying.") :
			MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicSource source = (MagicSource)data[0];
			final Collection<MagicTarget> creatures =
                game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget creature : creatures) {
				final MagicDamage damage = new MagicDamage(source,creature,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
}
