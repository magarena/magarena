package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Nagao__Bound_by_Honor {
    public static final MagicWhenAttacksTrigger T3 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = creature.getController();
            return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        "Samurai creatures " + player +
                        " controls get +1/+1 until end of turn.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
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
