package magic.model.event;

import magic.model.condition.MagicCondition;

public abstract class MagicOverloadActivation extends MagicHandCastActivation {

    public MagicOverloadActivation(final MagicTiming timing) {
        super(
            new MagicCondition[]{MagicCondition.CARD_CONDITION},
            new MagicActivationHints(timing, true),
            "Overload"
        );
    }
}
