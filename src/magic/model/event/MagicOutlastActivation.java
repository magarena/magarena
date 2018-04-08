package magic.model.event;

import java.util.List;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.condition.MagicCondition;

public class MagicOutlastActivation extends MagicPermanentActivation{

    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump);
    private static final MagicCondition COND[] = new MagicCondition[] {MagicCondition.SORCERY_CONDITION};
    private final List<MagicMatchedCostEvent> matchedCostEvents;

    public MagicOutlastActivation(final List<MagicMatchedCostEvent> aMatchedCostEvents) {
        super(COND, HINT, "Outlast");
        matchedCostEvents = aMatchedCostEvents;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return MagicMatchedCostEvent.getCostEvent(matchedCostEvents, source);
    }

    @Override
    public MagicEvent getPermanentEvent(MagicPermanent source, MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "PN puts a +1/+1 counter on SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, 1));
    }
}
