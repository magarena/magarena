package magic.card;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapEvent;

public class Mox_Opal {
    private static final MagicCondition[] CONDITIONS= {
        MagicCondition.CAN_TAP_CONDITION,
        MagicCondition.METALCRAFT_CONDITION};
    
    public static final MagicManaActivation MANA = new MagicManaActivation(MagicManaType.ALL_TYPES,CONDITIONS,2) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
            return new MagicEvent[]{new MagicTapEvent(permanent)};
        }    
    };
}
