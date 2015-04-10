package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.action.MagicPutItemOnStackAction;

import java.util.Arrays;

public class MagicCyclingActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;

    protected MagicCyclingActivation(final MagicManaCost aCost, final String name) {
        super(
            new MagicActivationHints(MagicTiming.Main,true),
            name
        );
        cost = aCost;
    }
    
    public MagicCyclingActivation(final MagicManaCost aCost) {
        this(aCost, "Cycle");
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
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
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicCard card = event.getCard();
                    final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                        MagicCyclingActivation.this,
                        getCardEvent(card, game.getPayedCost())
                    );
                    game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
                    game.executeTrigger(MagicTriggerType.WhenOtherCycle, card);
                    for (final MagicTrigger<MagicCard> trigger : card.getCardDefinition().getCycleTriggers()) {
                        game.executeTrigger(
                            trigger,
                            MagicPermanent.NONE,
                            card,
                            card
                        );
                    }
                }
            },
            name + " SN."
        );
    }
}
