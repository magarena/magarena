package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicAbilityOnStack;

import java.util.Arrays;

public class MagicCyclingActivation extends MagicCardActivation {

    final MagicManaCost cost;

    public MagicCyclingActivation(final MagicManaCost aCost) {
        super(
            new MagicActivationHints(MagicTiming.Main,true),
            "Cycle"
        );
        cost = aCost;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicDiscardSelfEvent(source)
        );
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Cycle SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
            this,
            new MagicDrawEvent(event.getSource(), event.getPlayer(), 1)
        );
        game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return MagicTargetChoice.NONE;
    }
}
