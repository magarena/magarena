package magic.model.event;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

import java.util.List;

public class MagicSacrificeTapManaActivation extends MagicManaActivation {

    private static final MagicCondition[] CONDITIONS= {MagicCondition.CAN_TAP_CONDITION};
            
    public MagicSacrificeTapManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,CONDITIONS,3);
    }

    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
        return new MagicEvent[]{
            new MagicTapEvent(permanent),
            new MagicSacrificeEvent(permanent)
        };
    }    
}
