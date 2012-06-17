package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Seedborn_Muse {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player != data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "Untap all permanents you control."):
                MagicEvent.NONE;
        }    
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = (MagicPlayer)data[0];
            final Collection<MagicTarget> targets =
                game.filterTargets(player,MagicTargetFilter.TARGET_PERMANENT_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicUntapAction((MagicPermanent)target));
            }
        }
    };
}
