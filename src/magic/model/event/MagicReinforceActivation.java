package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

import java.util.Arrays;

public class MagicReinforceActivation extends MagicCardActivation {

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

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicDiscardCardEvent(
                source,
                source.getController(),
                source
            )
        );
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Reinforce."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
            this,
            new MagicPutCounterEvent(event.getSource(), amount),
            game.getPayedCost()
        );
        game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return MagicTargetChoice.POS_TARGET_CREATURE;
    }
}
