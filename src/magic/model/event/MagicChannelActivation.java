package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicChoice;
import magic.model.stack.MagicAbilityOnStack;

import java.util.Arrays;

public abstract class MagicChannelActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;

    public MagicChannelActivation(final String manaCost, final MagicActivationHints hints) {
        super(
            hints,
            "Channel"
        );
        cost = MagicManaCost.create(manaCost);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicDiscardSelfEvent(source)
        );
    }
}
