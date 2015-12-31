package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;

import java.util.Arrays;

public class MagicReinforceActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;
    final int amount;

    public MagicReinforceActivation(final int n, final MagicManaCost aCost) {
        super(
            new MagicActivationHints(MagicTiming.Pump,true),
            "Reinforce"
        );
        cost = aCost;
        amount = n;
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
        return new MagicPutCounterEvent(card, amount);
    }
}
