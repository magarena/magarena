package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public abstract class MagicChannelActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;

    public MagicChannelActivation(final MagicCondition[] conditions, final String manaCost, final MagicActivationHints hints) {
        super(
            conditions,
            hints,
            "Channel"
        );
        cost = MagicManaCost.create(manaCost);
    }
    
    public MagicChannelActivation(final String manaCost, final MagicActivationHints hints) {
        this(MagicChannelActivation.NO_COND, manaCost, hints);
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
