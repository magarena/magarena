package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicMorphActivation extends MagicPermanentActivation {
    
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump);
    private static final MagicCondition COND[] = new MagicCondition[]{ MagicCondition.FACE_DOWN_PERMANENT_CONDITION };
    private final MagicManaCost cost;

    public MagicMorphActivation(final MagicManaCost aCost) {
        super(COND, HINT, "Morph");
        cost = aCost;
    }
    
    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Arrays.asList(new MagicPayManaCostEvent(source,cost));
    }
    
    //Doesn't use the stack
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "PN turns SN face up."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(MagicChangeStateAction.Clear(event.getPermanent(), MagicPermanentState.FaceDown));
    }
}
