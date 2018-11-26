package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.ChangeCountersAction;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;

import java.util.Collections;

public class MagicLevelUpActivation extends MagicPermanentActivation {

    private static final MagicActivationHints ACTIVATION_HINTS =
        new MagicActivationHints(MagicTiming.Main);

    private final MagicManaCost cost;

    public MagicLevelUpActivation(final MagicManaCost cost,final int maximum) {
        super(
            new MagicCondition[]{
                MagicCondition.SORCERY_CONDITION,
                new MagicArtificialCondition(
                    new MaximumCondition(maximum)
                ),
            },
            ACTIVATION_HINTS,
            "Level"
        );
        this.cost=cost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Collections.singletonList(new MagicPayManaCostEvent(source, cost));
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Put a level counter on SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.Level,1));
    }

    private static final class MaximumCondition extends MagicCondition {

        private final int maximum;

        public MaximumCondition(final int maximum) {
            this.maximum=maximum;
        }

        @Override
        public boolean accept(final MagicSource source) {
            return ((MagicPermanent)source).getCounters(MagicCounterType.Level)<maximum;
        }
    }
}
