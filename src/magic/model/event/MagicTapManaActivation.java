package magic.model.event;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

import java.util.List;

public class MagicTapManaActivation extends MagicManaActivation {

    public MagicTapManaActivation(final List<MagicManaType> manaTypes,final int weight) {
        super(manaTypes,weight);
    }

    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent perm) {
        return new MagicEvent[]{
            new MagicTapEvent(perm)
        };
    }
}
