package magic.model.event;

import java.util.Collections;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.action.ChangeCountersAction;
import magic.model.action.ShiftCardAction;

public class MagicSuspendActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;
    final int amount;

    public MagicSuspendActivation(final int aAmount, final MagicManaCost aCost) {
        super(
            CARD_CONDITION,
            new MagicActivationHints(MagicTiming.Main,true),
            "Suspend"
        );
        cost = aCost;
        amount = aAmount;
    }

    @Override
    boolean usesStack() {
        return false;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Collections.singletonList(
            new MagicPayManaCostEvent(source, cost)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost) {
        return MagicEvent.NONE;
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            amount,
            this::suspend,
            "PN suspends SN. PN exiles SN, with RN time counters on it."
        );
    }

    private void suspend(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersHand, MagicLocationType.Exile));
        game.doAction(new ChangeCountersAction(card.getController(), card, MagicCounterType.Time, event.getRefInt()));
    }
}
