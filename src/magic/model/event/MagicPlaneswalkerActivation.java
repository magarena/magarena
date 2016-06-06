package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicPermanent;
import magic.model.MagicPayedCost;
import magic.model.condition.MagicCondition;
import magic.model.ARG;

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

    public static final MagicPlaneswalkerActivation create(final String act) {
        final String[] part = act.split(ARG.COLON, 2);

        // build the actual costs
        final int cost = Integer.parseInt(part[0].replace('−', '-'));

        // parse the effect
        final String rule = part[1];
        final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(rule);


        return new MagicPlaneswalkerActivation(cost, part[0]) {
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
                return sourceEvent.getEvent(source, payedCost);
            }
        };
    }
}
