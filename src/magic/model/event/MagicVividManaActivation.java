package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;

import java.util.List;

public class MagicVividManaActivation extends MagicManaActivation {

    public MagicVividManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,manaTypes.size() -1);
    }
        
    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
        return new MagicEvent[]{
            new MagicTapEvent(permanent),
            new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)
        };
    }    
}
