package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Riot_Ringleader {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicEvent.NO_DATA,
                        this,
                        "Human creatures " + permanent.getController() +
                        " controls get +1/+0 until end of turn."):
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
                    MagicTargetFilter.TARGET_HUMAN_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicChangeTurnPTAction(
                        (MagicPermanent)target,
                        1,
                        0));
            }
        }
    };
}
