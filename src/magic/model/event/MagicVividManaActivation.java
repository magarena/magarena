package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;

import java.util.Arrays;
import java.util.List;

public class MagicVividManaActivation extends MagicManaActivation {

    public MagicVividManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent permanent) {
        return Arrays.asList(
            new MagicTapEvent(permanent),
            new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)
        );
    }
}
