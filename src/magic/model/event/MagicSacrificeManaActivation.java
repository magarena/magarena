package magic.model.event;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import java.util.Collections;
import java.util.List;

public class MagicSacrificeManaActivation extends MagicManaActivation {

    public MagicSacrificeManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,3);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent perm) {
        return Collections.singletonList(new MagicSacrificeEvent(perm));
    }
}
