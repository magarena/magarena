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
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

import java.util.Collection;

public class Thunder_Dragon {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN deals 3 damage to each creature without flying.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicSource source=event.getSource();
            final Collection<MagicPermanent> creatures=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage=new MagicDamage(source,creature,3);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
}
