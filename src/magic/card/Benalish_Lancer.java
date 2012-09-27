package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Benalish_Lancer {                 
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
                        2,
                        true));
                game.doAction(new MagicSetAbilityAction(permanent,MagicAbility.FirstStrike,MagicStatic.Forever));
            }
            return MagicEvent.NONE;
        }
    };
}
