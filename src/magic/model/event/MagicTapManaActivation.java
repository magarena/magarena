package magic.model.event;

import java.util.Collections;
import java.util.List;

import magic.model.MagicManaType;
import magic.model.MagicPermanent;

public class MagicTapManaActivation extends MagicManaActivation {

    public MagicTapManaActivation(final List<MagicManaType> manaTypes) {
        super(manaTypes);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent perm) {
        return Collections.singletonList(new MagicTapEvent(perm));
    }

    public static final MagicManaActivation White = new MagicTapManaActivation(MagicManaType.getList("{W}"));
    public static final MagicManaActivation Blue  = new MagicTapManaActivation(MagicManaType.getList("{U}"));
    public static final MagicManaActivation Black = new MagicTapManaActivation(MagicManaType.getList("{B}"));
    public static final MagicManaActivation Red   = new MagicTapManaActivation(MagicManaType.getList("{R}"));
    public static final MagicManaActivation Green = new MagicTapManaActivation(MagicManaType.getList("{G}"));
}
