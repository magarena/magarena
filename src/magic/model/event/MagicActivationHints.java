package magic.model.event;

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
