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
import java.util.List;
import java.util.LinkedList;

public class MagicMorphActivation extends MagicPermanentActivation {
    
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump);
    private static final MagicCondition COND[] = new MagicCondition[]{ MagicCondition.FACE_DOWN_PERMANENT_CONDITION };
    private final List<MagicMatchedCostEvent> matchedCostEvents;

    public MagicMorphActivation(final List<MagicMatchedCostEvent> aMatchedCostEvents) {
        super(COND, HINT, "Morph");
        matchedCostEvents = aMatchedCostEvents;
    }
    
    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        final List<MagicEvent> costEvents = new LinkedList<MagicEvent>();
        for (final MagicMatchedCostEvent matched : matchedCostEvents) {
            costEvents.add(matched.getEvent(source));
        }
        return costEvents;
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
