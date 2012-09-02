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

public class Crater_Hellion {
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    permanent + " deals 4 damage to each other creature.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicSource permanent = event.getPermanent();
            final Collection<MagicTarget> creatures =
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicTarget creature : creatures) {
                if (permanent != creature) {
                    final MagicDamage damage=new MagicDamage(permanent,creature,4,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    };
}
