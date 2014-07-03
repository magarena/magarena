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

public abstract class MagicChannelActivation extends MagicCardActivation {

    final MagicManaCost cost;

    public MagicChannelActivation(final String manaCost, final MagicActivationHints hints) {
        super(
            hints,
            "Channel"
        );
        cost = MagicManaCost.create(manaCost);
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

    public abstract MagicEvent getCardEvent(final MagicSource source, final MagicPayedCost payedCost);

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                        MagicChannelActivation.this,
                        getCardEvent(event.getSource(), game.getPayedCost())
                    );
                    game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
                }
            },
            "Channel SN."
        );
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return getCardEvent(source, MagicPayedCost.NO_COST).getChoice();
    }
}
