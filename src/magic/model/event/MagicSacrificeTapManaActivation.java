package magic.model.event;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

import java.util.List;

public class MagicSacrificeTapManaActivation extends MagicManaActivation {

    public MagicSacrificeTapManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,3);
    }

    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
        return new MagicEvent[]{
            new MagicTapEvent(permanent),
            new MagicSacrificeEvent(permanent)
        };
    }
}
