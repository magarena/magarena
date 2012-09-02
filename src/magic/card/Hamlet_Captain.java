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
import magic.model.trigger.MagicWhenBlocksTrigger;


public class Hamlet_Captain {
    public static final MagicWhenAttacksTrigger T1 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        this,
                        "Other Human creatures " + permanent.getController() +
                        " controls get +1/+1 until end of turn."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets =
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_HUMAN_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature = (MagicPermanent)target;
                if (creature != event.getPermanent()) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                }
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        this,
                        "Other Human creatures " + permanent.getController() +
                        " controls get +1/+1 until end of turn."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets =
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_HUMAN_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature = (MagicPermanent)target;
                if (creature != event.getPermanent()) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                }
            }
        }
    };
}
