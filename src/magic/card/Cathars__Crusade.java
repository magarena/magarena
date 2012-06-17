package magic.card;

import java.util.Collection;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Cathars__Crusade {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (otherPermanent.isCreature() && 
                otherPermanent.getController() == player) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicEvent.NO_DATA,
                        this,
                        player + " puts a +1/+1 counter on " +
                        "each creature he or she controls.") :
               MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets = game.filterTargets(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)target,
                        MagicCounterType.PlusOne,
                        1,
                        true));
            }
        }
    };
}
