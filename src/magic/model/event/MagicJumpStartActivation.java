package magic.model.event;

import java.util.Arrays;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicSource;
import magic.model.action.CastCardAction;

public class MagicJumpStartActivation extends MagicGraveyardCastActivation {

    public MagicJumpStartActivation(final MagicCardDefinition cdef) {
        super(
            MagicHandCastActivation.CARD_CONDITION,
            cdef.getActivationHints(),
            "Jump-start"
        );
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(new MagicDiscardEvent(source));
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Jump-start SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new CastCardAction(
            event.getPlayer(),
            event.getCard(),
            MagicLocationType.Graveyard,
            MagicLocationType.Exile
        ));
    }
}
