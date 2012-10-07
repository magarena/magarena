package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

import java.util.Collection;

public class Nagao__Bound_by_Honor {
    public static final MagicWhenAttacksTrigger T3 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Samurai creatures PN controls get +1/+1 until end of turn."
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets =
                    game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_SAMURAI_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature = (MagicPermanent)target;
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
            }
        }
    };
}
