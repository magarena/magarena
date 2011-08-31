package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class Thunder_Dragon {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,player},
                    this,
                    permanent + " deals 3 damage to each creature without flying.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicSource source=(MagicSource)data[0];
			final Collection<MagicTarget> creatures=
                game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget creature : creatures) {
				final MagicDamage damage=new MagicDamage(source,creature,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
}
