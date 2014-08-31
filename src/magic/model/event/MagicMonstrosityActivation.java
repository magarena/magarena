package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicMonstrosityActivation extends MagicPermanentActivation {
    
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump);
    private static final MagicCondition COND[] = new MagicCondition[]{ MagicCondition.NOT_MONSTROUS_CONDITION };
    private final MagicManaCost cost;
    private final int n;

    public MagicMonstrosityActivation(final MagicManaCost aCost,final int aN) {
        super(COND, HINT, "Monstrosity");
        cost = aCost;
        n = aN;
    }
    
    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Arrays.asList(new MagicPayManaCostEvent(source,cost));
    }
    
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "If SN isn't monstrous, put " + n + " +1/+1 counter on it and it becomes monstrous."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeCountersAction(
            event.getPermanent(),
            MagicCounterType.PlusOne,
            n
        ));
        game.doAction(MagicChangeStateAction.Set(
            event.getPermanent(),
            MagicPermanentState.Monstrous
        ));
    }
}
