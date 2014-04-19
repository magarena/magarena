package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicPermanent;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public abstract class MagicPlaneswalkerActivation extends MagicPermanentActivation {

    final int cost;

    public MagicPlaneswalkerActivation(final int cost) {
        this(cost, (cost > 0 ? "+" : "") + cost);
    }

    public MagicPlaneswalkerActivation(final int aCost, final String description) {
        super(
            new MagicCondition[] {
                MagicCondition.SORCERY_CONDITION
            },
            new MagicActivationHints(MagicTiming.Main),
            description
        );
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Arrays.asList(
            cost >= 0 ?
                MagicPutCounterEvent.Self(
                    source,
                    MagicCounterType.Loyalty,
                    cost
                ):
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Loyalty,
                    -cost
                ),
            new MagicPlayAbilityEvent(source)
        );
    }
}
