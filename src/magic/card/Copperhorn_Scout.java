package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

import java.util.Collection;

public class Copperhorn_Scout {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent attacker) {
            return (permanent == attacker) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap each other creature you control."
                ):
                MagicEvent.NONE;
        }    
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent other : targets) {
                if (other != permanent) {
                    game.doAction(new MagicUntapAction(other));
                }
            }
        }        
    };
}
