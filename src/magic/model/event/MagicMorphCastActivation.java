package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayMod;
import magic.model.action.MagicRemoveCardAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicMorphCastActivation extends MagicCardActivation {

    public MagicMorphCastActivation() {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Pump, true),
            "Morph"
        );
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                MagicManaCost.create("{3}")
            )
        );
    }
    
    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Play a face down card."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicRemoveCardAction(event.getCard(), MagicLocationType.OwnersHand));
        game.doAction(new MagicPlayCardAction(event.getCard(),event.getPlayer(),MagicPlayMod.FACE_DOWN));
    }
    
}
