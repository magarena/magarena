package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;

import java.util.Arrays;

public class MagicCyclingActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;

    public MagicCyclingActivation(final MagicManaCost aCost) {
        super(
            new MagicActivationHints(MagicTiming.Main,true),
            "Cycle"
        );
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
            new MagicDiscardSelfEvent(source)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost) {
        return new MagicDrawEvent(card, card.getController(), 1);
    }
}
