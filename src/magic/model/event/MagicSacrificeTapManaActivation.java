package magic.model.event;

import java.util.Arrays;
import java.util.List;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;

public class MagicSacrificeTapManaActivation extends MagicManaActivation {

    public MagicSacrificeTapManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,3);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent permanent) {
        return Arrays.asList(
            new MagicTapEvent(permanent),
            new MagicSacrificeEvent(permanent)
        );
    }
}
