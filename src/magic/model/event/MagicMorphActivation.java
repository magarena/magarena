package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicCardDefinition;
import magic.model.MagicSource;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicTurnFaceUpAction;
import magic.model.condition.MagicCondition;
import magic.model.trigger.MagicTriggerType;

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
    
    @Override
    public final MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            ""
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicTurnFaceUpAction(event.getPermanent()));
        game.logAppendMessage(event.getPlayer(), event.getPlayer() + " turns " + event.getPermanent() + " face up.");
    }
    
    @Override
    public boolean usesStack() {
        return false;
    }
    
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return MagicEvent.NONE;
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addMorphAct(this);
    }
}
