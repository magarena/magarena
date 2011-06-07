package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.condition.*;
import magic.model.*;

public class Mox_Opal {
    private static final MagicCondition CONDITIONS[]=new MagicCondition[]{
        MagicCondition.CAN_TAP_CONDITION,
        MagicCondition.METALCRAFT_CONDITION};
    
    public static final MagicManaActivation MANA = new MagicManaActivation(MagicManaType.ALL_TYPES,CONDITIONS,2) {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return new MagicEvent[]{new MagicTapEvent(permanent)};
        }	
    };
    
}
