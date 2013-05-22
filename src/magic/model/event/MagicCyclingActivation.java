package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

public class MagicCyclingActivation extends MagicCardActivation {

    final MagicManaCost cost;

    public MagicCyclingActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                aCost.getCondition()
            },
            new MagicActivationHints(MagicTiming.Main,true),
            "Cycle"
        );
        cost = aCost;
    }
  
    public MagicEvent[] getCostEvent(final MagicCard source) {
        return new MagicEvent[]{
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicDiscardCardEvent(
                source,
                source.getController(),
                source
            )
        };
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
            new MagicDrawEvent(event.getSource(), event.getPlayer(), 1), 
            game.getPayedCost()
        );
        game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
    }

    @Override
    final MagicTargetChoice getTargetChoice(final MagicCard source) {
        return MagicTargetChoice.NONE;
    }
}
