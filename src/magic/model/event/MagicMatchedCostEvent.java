package magic.model.event;

import magic.model.MagicSource;

public interface MagicMatchedCostEvent {
    MagicEvent getEvent(final MagicSource source);
    boolean isIndependent();
}
