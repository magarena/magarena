package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

import java.util.Collection;

public class Seedborn_Muse {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap all permanents you control."
                ):
                MagicEvent.NONE;
        }    
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(player,MagicTargetFilter.TARGET_PERMANENT_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicUntapAction(target));
            }
        }
    };
}
