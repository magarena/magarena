package magic.model.event;

import java.util.Arrays;
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

    protected MagicSuspendActivation(final int aAmount, final MagicManaCost aCost, final String name) {
        super(
            new MagicActivationHints(MagicTiming.Main, true),
            name
        );
        cost = aCost;
        amount = aAmount;
    }

    public MagicSuspendActivation(final int aAmount, final MagicManaCost aCost) {
        this(aAmount, aCost, "Suspend");
    }

    @Override
    boolean usesStack() {
        return false;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard card, final MagicPayedCost payedCost) {
        return new MagicEvent(
            card,
            this,
            ""
        );
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            (final MagicGame game, final MagicEvent event) -> {
                final MagicCard card = event.getCard();
                game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersHand, MagicLocationType.Exile));
                game.doAction(new ChangeCountersAction(card, MagicCounterType.Time, amount));
            },
            "PN suspends SN. PN exiles SN, with "+amount+" time counters on it."
        );
    }

}
