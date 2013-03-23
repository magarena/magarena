package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

import java.util.Collection;

public class Timbermare {
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
                    "Tap all other creatures.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent creature : targets) {
                if (creature != event.getPermanent()) {
                    game.doAction(new MagicTapAction(creature,true));
                }
            }
        }
    };
}
