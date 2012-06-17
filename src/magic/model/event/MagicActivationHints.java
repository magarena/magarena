package magic.model.event;

import magic.model.MagicPermanent;
import magic.model.MagicSource;

public class MagicActivationHints {
    
    /** timing */
    private final MagicTiming timing;
    /** source independent */
    private final boolean independent; 
    /** maximum number of ability activations each turn */
    private final int maximum; 
    
    public MagicActivationHints(final MagicTiming timing,final boolean independent,final int maximum) {
        this.timing=timing;
        this.independent=independent;
        this.maximum=maximum;
    }
    
    public MagicActivationHints(final MagicTiming timing,final boolean independent) {
        this(timing,independent,0);
    }
    
    public MagicActivationHints(final MagicTiming timing) {
        this(timing,false);
    }
    
    MagicTiming getTiming() {
        return timing;
    }
    
    public boolean isIndependent() {
        return independent;
    }
    
    private int getMaximum() {
        return maximum;
    }
    
    boolean isMaximum(final MagicSource source) {
        if (maximum==0) {
            return false;
        }
        final MagicPermanent permanent=(MagicPermanent)source;
        return permanent.getAbilityPlayedThisTurn()>=maximum;
    }
}
