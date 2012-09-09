package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicStatic;

public class Llanowar_Elite {                 
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            if (permanent.isKicked()) {
                game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.PlusOne,
                        5,
                        true));
            }
            return MagicEvent.NONE;
        }
    };
}
