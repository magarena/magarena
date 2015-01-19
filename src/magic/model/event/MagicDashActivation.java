package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayMod;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicDashActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;

    public MagicDashActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            new MagicActivationHints(MagicTiming.FirstMain,true),
            "Dash"
        );
        cost = aCost;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            )
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Put SN onto the battlefield, "
            "it gains haste and it's returned from the battlefield to its owner's hand at the beginning of the next end step."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
        game.doAction(new MagicPlayCardAction(
            card,
            event.getPlayer(),
            MagicPlayMod.HASTE, MagicPlayMod.RETURN_AT_END_OF_TURN
        ));
    }
}
