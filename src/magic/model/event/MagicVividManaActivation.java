package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;

import java.util.List;
import java.util.Arrays;

public class MagicVividManaActivation extends MagicManaActivation {

    public MagicVividManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,manaTypes.size() -1);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent permanent) {
        return Arrays.asList(
            new MagicTapEvent(permanent),
            new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)
        );
    }
}
