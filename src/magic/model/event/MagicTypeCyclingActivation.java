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

public class MagicTypeCyclingActivation extends MagicCardActivation {

    final MagicManaCost cost;
    final String type;
    static final MagicCondition[] HAND_CONDITION = new MagicCondition[] { MagicCondition.HAND_CONDITION };

    public MagicTypeCyclingActivation(final MagicManaCost aCost, final String aType) {
        super(
            HAND_CONDITION,
            new MagicActivationHints(MagicTiming.Main,true),
            aType
        );
        cost = aCost;
        type = aType;
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
            type+"cycle SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
            this,
            new MagicSearchIntoHandEvent(event.getSource(), event.getPlayer(), new MagicTargetChoice("a "+type+" card from your library")),
            game.getPayedCost()
        );
        game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return MagicTargetChoice.NONE;
    }
}
