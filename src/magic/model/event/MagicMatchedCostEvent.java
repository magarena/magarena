package magic.model.event;

import java.util.List;
import java.util.stream.Collectors;

import magic.model.MagicSource;

public interface MagicMatchedCostEvent {
    MagicEvent getEvent(final MagicSource source);
    boolean isIndependent();

    public static List<MagicEvent> getCostEvent(final List<MagicMatchedCostEvent> costs, final MagicSource source) {
        return costs.stream()
            .map(it -> it.getEvent(source))
            .collect(Collectors.toList());
    }
}
