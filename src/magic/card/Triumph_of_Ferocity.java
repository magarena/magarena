package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

import java.util.Collection;

public class Triumph_of_Ferocity {
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
                    "PN draws a card if he or she " +
                    "controls the creature with the greatest " +
                    "power or tied for the greatest power.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    player,
                    MagicTargetFilter.TARGET_CREATURE);
            MagicPermanent highest = MagicPermanent.NONE;   
            for (final MagicPermanent creature : targets) {
                if (creature.getPower() > highest.getPower()) {
                    highest = creature;
                }
            }
            if (highest.getController() == player) {
                game.doAction(new MagicDrawAction(player,1));
            }
        }       
    };
}
