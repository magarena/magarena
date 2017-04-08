package magic.model.event;

import java.util.Arrays;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.PutItemOnStackAction;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class MagicCyclingActivation extends MagicCardAbilityActivation {

    final MagicMatchedCostEvent matchedCost;

    protected MagicCyclingActivation(final MagicMatchedCostEvent aMatchedCost, final String name) {
        super(
            new MagicActivationHints(MagicTiming.Main,true),
            name
        );
        matchedCost = aMatchedCost;
    }

    public MagicCyclingActivation(final MagicMatchedCostEvent aMatchedCost) {
        this(aMatchedCost, "Cycle");
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            matchedCost.getEvent(source),
            new MagicDiscardSelfEvent(source)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost) {
        return new MagicDrawEvent(card, card.getController(), 1);
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            (final MagicGame game, final MagicEvent event) -> {
                final MagicCard card = event.getCard();
                final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                    MagicCyclingActivation.this,
                    getCardEvent(card, game.getPayedCost())
                );
                game.doAction(new PutItemOnStackAction(abilityOnStack));
                game.executeTrigger(MagicTriggerType.WhenOtherCycle, card);
                for (final MagicTrigger<MagicCard> trigger : card.getCardDefinition().getCycleTriggers()) {
                    game.executeTrigger(
                        trigger,
                        MagicPermanent.NONE,
                        card,
                        card
                    );
                }
            },
            name + " SN."
        );
    }
}
