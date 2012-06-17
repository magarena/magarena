package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;


import java.util.Collection;

public class Goblin_Piledriver {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        permanent + " gets +2/+0 until end of turn for each other attacking Goblin."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            int power=0;
            final MagicPermanent creature=(MagicPermanent)data[0];
            final Collection<MagicTarget> targets=
                game.filterTargets(creature.getController(),MagicTargetFilter.TARGET_ATTACKING_CREATURE);
            for (final MagicTarget target : targets) {
                if (creature!=target) {
                    final MagicPermanent attacker=(MagicPermanent)target;
                    if (attacker.hasSubType(MagicSubType.Goblin)) {
                        power+=2;
                    }
                }
            }
            if (power>0) {
                game.doAction(new MagicChangeTurnPTAction(creature,power,0));
            }
        }
    };
}
