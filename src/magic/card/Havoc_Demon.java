package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenDiesTrigger;

import java.util.Collection;

public class Havoc_Demon {
    public static final MagicWhenDiesTrigger T = new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                this,
                "All creatures get -5/-5 until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets=
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,-5,-5));
            }
        }
    };
}
