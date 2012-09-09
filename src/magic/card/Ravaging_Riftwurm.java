package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Ravaging_Riftwurm {
    public static final MagicFadeVanishCounterTrigger T = new MagicFadeVanishCounterTrigger("time");
    
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {    
            final int amount = permanent.isKicked() ? 5 : 2;
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.Charge,
                amount,
                true
            ));
            return MagicEvent.NONE;
        }
    };
}
