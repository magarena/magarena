package magic.model.event;

import java.util.LinkedList;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicSource;
import magic.model.action.CastCardAction;

public class MagicFlashbackActivation extends MagicGraveyardCastActivation {

    private final List<MagicMatchedCostEvent> matchedCostEvents;

    public MagicFlashbackActivation(final MagicCardDefinition cdef, final List<MagicMatchedCostEvent> aMatchedCostEvents) {
        super(
            MagicHandCastActivation.CARD_CONDITION,
            cdef.getActivationHints(),
            "Flashback"
        );
        matchedCostEvents = aMatchedCostEvents;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        final List<MagicEvent> costEvents = new LinkedList<MagicEvent>();
        for (final MagicMatchedCostEvent matched : matchedCostEvents) {
            costEvents.add(matched.getEvent(source));
        }
        return costEvents;
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Flashback SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(CastCardAction.WithoutManaCost(
            event.getPlayer(),
            event.getCard(),
            MagicLocationType.Graveyard,
            MagicLocationType.Exile
        ));
    }
}
