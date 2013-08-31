package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyable;
import magic.model.MagicCopyMap;
import magic.model.MagicCounterType;
import magic.model.MagicLocationType;
import magic.model.MagicPowerToughness;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

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
                    MagicCounterType.Charge,
                    cost
                ):
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Charge,
                    -cost
                ),
            new MagicPlayAbilityEvent(source)
        );
    }
}
