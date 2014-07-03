package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayMod;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

import java.util.Arrays;

public class MagicNinjutsuActivation extends MagicCardActivation {

    final MagicManaCost cost;

    public MagicNinjutsuActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.NINJUTSU_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Block,true),
            "Ninjutsu"
        );
        cost = aCost;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicBounceChosenPermanentEvent(
                source,
                MagicTargetChoice.AN_UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL
            )
        );
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                        MagicNinjutsuActivation.this,
                        getCardEvent(event.getCard(), game.getPayedCost())
                    );
                    game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
                }
            },
            "Ninjutsu."
        );
    }

    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Put SN onto the battlefield from your hand tapped and attacking."
        );
    }
                
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
        game.doAction(new MagicPlayCardAction(
            card,
            event.getPlayer(),
            MagicPlayMod.TAPPED, MagicPlayMod.ATTACKING
        ));
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return MagicTargetChoice.NONE;
    }
}
