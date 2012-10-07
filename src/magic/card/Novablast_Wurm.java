package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

import java.util.Collection;

public class Novablast_Wurm {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                        permanent,
                        this,
                        "Destroy all creatures other than SN.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicTargetFilter targetFilter = 
                    new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE,permanent);
            final Collection<MagicPermanent> targets=
                game.filterPermanents(permanent.getController(),targetFilter);
            game.doAction(new MagicDestroyAction(targets));
        }
    };
}
