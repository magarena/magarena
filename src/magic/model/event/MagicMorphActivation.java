package magic.model.event;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import magic.model.MagicGame;
import magic.model.MagicCardDefinition;
import magic.model.MagicSource;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.TurnFaceUpAction;
import magic.model.condition.MagicCondition;
import magic.model.MagicMessage;

public class MagicMorphActivation extends MagicPermanentActivation {

    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump);
    private static final MagicCondition COND[] = new MagicCondition[]{ MagicCondition.FACE_DOWN_PERMANENT_CONDITION };
    private final List<MagicMatchedCostEvent> matchedCostEvents;

    public static final MagicMorphActivation Manifest = new MagicMorphActivation(Collections.emptyList()) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return Collections.singletonList(
                new MagicPayManaCostEvent(
                    source,
                    source.getRealCardDefinition().getCost()
                )
            );
        }
    };

    public MagicMorphActivation(final List<MagicMatchedCostEvent> aMatchedCostEvents) {
        this(aMatchedCostEvents, "Morph");
    }

    protected MagicMorphActivation(final List<MagicMatchedCostEvent> aMatchedCostEvents, final String name) {
        super(COND, HINT, name);
        matchedCostEvents = aMatchedCostEvents;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        final List<MagicEvent> costEvents = new LinkedList<>();
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
        game.doAction(new TurnFaceUpAction(event.getPermanent()));
        game.logAppendMessage(
            event.getPlayer(),
            MagicMessage.format("%s turns %s face up.", event.getPlayer(), event.getPermanent())
        );
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
