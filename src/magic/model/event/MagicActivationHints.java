package magic.model.event;

import magic.model.MagicPermanent;
import magic.model.MagicSource;

public class MagicActivationHints {

    /** timing */
    private final MagicTiming timing;

    /** source independent */
    private final boolean independent;

    public MagicActivationHints(final MagicTiming aTiming, final boolean aIndependent) {
        timing = aTiming;
        independent = aIndependent;
    }

    public MagicActivationHints(final MagicTiming timing) {
        this(timing, false);
    }

    public MagicTiming getTiming() {
        return timing;
    }

    public boolean isIndependent() {
        return independent;
    }
}
