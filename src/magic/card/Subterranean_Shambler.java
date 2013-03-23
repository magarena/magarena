package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

import java.util.Collection;

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
                    this,
                    "SN deals 1 damage to each creature without flying.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicSource source = event.getSource();
            final Collection<MagicPermanent> creatures =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage = new MagicDamage(source,creature,1);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
    
    public static final MagicWhenLeavesPlayTrigger T3 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent left) {
            return (permanent == left) ?
                new MagicEvent(
                        permanent,
                        this,
                        "SN deals 1 damage to each creature without flying.") :
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicSource source = event.getSource();
            final Collection<MagicPermanent> creatures =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage = new MagicDamage(source,creature,1);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
}
