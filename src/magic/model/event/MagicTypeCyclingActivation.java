package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Arrays;

public class MagicTypeCyclingActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;
    final String type;

    public MagicTypeCyclingActivation(final MagicManaCost aCost, final String aType) {
        super(
            new MagicActivationHints(MagicTiming.Main,true),
            aType + "cycle"
        );
        cost = aCost;
        type = aType;
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
        return new MagicEvent(
            card,
            this,
            "PN searches his or her library for a " + type + " card. " +
            "Then PN shuffles his or her library."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.addEvent(new MagicSearchToLocationEvent(
            event, 
            new MagicTargetChoice("a "+type+" card from your library"),
            MagicLocationType.OwnersHand
        ));
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
                        MagicTypeCyclingActivation.this,
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
