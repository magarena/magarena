package magic.model.event;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;

import java.util.Arrays;
import java.util.List;

public class MagicTapDepleteManaActivation extends MagicManaActivation {

    public MagicTapDepleteManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent perm) {
        return Arrays.asList(new MagicTapDepleteEvent(perm));
    }
}
