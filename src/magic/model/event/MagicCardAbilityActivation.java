package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

public abstract class MagicCardAbilityActivation extends MagicCardActivation {

    final String name;

    public MagicCardAbilityActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String aName) {
        super(
            conditions,
            hints,
            aName
        );
        name = aName;
    }
    
    public MagicCardAbilityActivation(final MagicActivationHints hints, final String aName) {
        this(MagicActivation.NO_COND, hints, aName);
    }

    public abstract MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost);

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                        MagicCardAbilityActivation.this,
                        getCardEvent(event.getCard(), game.getPayedCost())
                    );
                    game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
                }
            },
            name + " SN."
        );
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return getCardEvent(source, MagicPayedCost.NO_COST).getChoice();
    }
}
