package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicPumpTargetPicker;

import java.util.Arrays;

public abstract class MagicBloodrushActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;
    final String desc;

    public MagicBloodrushActivation(final MagicManaCost aCost, final String aDesc) {
        super(
            new MagicActivationHints(MagicTiming.Pump,true),
            "Bloodrush"
        );
        cost = aCost;
        desc = aDesc;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
            new MagicDiscardSelfEvent(source)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.POS_TARGET_ATTACKING_CREATURE,
            MagicPumpTargetPicker.create(),
            this,
            desc
        );
    }
}
