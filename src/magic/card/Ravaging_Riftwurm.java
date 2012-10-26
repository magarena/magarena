package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Ravaging_Riftwurm {
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {    
            if (permanent.isKicked()) {
                game.doAction(new MagicChangeCountersAction(
                    permanent,
                    MagicCounterType.Charge,
                    3,
                    true
                ));
            }
            return MagicEvent.NONE;
        }
    };
}
