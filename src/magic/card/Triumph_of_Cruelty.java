package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

import java.util.Collection;

public class Triumph_of_Cruelty {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    upkeepPlayer.getOpponent() + " discards a card " +
                    "if you control the creature with the greatest " +
                    "power or tied for the greatest power.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicTarget> targets = game.filterTargets(
                    player,
                    MagicTargetFilter.TARGET_CREATURE);
            MagicPermanent highest = MagicPermanent.NONE;     
            for (final MagicTarget target : targets) {
                final MagicPermanent creature = (MagicPermanent)target;
                if (creature.getPower() > highest.getPower()) {
                    highest = creature;
                }
            }
            if (highest.getController() == player) {
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    player.getOpponent(),
                    1,
                    false
                ));
            }
        }        
    };
}
