package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.target.MagicPumpTargetPicker;

public abstract class MagicBloodrushActivation extends MagicCardActivation {

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
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                        MagicBloodrushActivation.this,
                        getCardEvent(event.getCard(), game.getPayedCost()), 
                        game.getPayedCost()
                    );
                    game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
                }
            },
            "Bloodrush."
        );
    }
    
    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.POS_TARGET_ATTACKING_CREATURE,
            MagicPumpTargetPicker.create(),
            this,
            desc
        );
    }

    @Override
    final MagicTargetChoice getTargetChoice(final MagicCard source) {
        return getCardEvent(source,MagicPayedCost.NO_COST).getTargetChoice();
    }
}
