package magic.model.event;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicPainTapEvent;

import java.util.List;

public class MagicPainTapManaActivation extends MagicManaActivation {

    private static final MagicCondition[] CONDITIONS = {
        MagicCondition.CAN_TAP_CONDITION
    };
            
    public MagicPainTapManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,CONDITIONS,manaTypes.size() - 1);
    }
        
    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent perm) {
        return new MagicEvent[] {
            new MagicPainTapEvent(perm)
        };
    }
}
