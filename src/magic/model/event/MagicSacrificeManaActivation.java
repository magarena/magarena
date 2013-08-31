package magic.model.event;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

import java.util.List;
import java.util.Arrays;

public class MagicSacrificeManaActivation extends MagicManaActivation {

    private static final MagicCondition[] CONDITION=new MagicCondition[0];

    public MagicSacrificeManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes,CONDITION,3);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent perm) {
        return Arrays.asList(new MagicSacrificeEvent(perm));
    }
}
