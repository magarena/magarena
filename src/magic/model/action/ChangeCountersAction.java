package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class ChangeCountersAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicCard card;
    private final MagicCounterType counterType;
    private final int amount;
    private final boolean hasScore;

    private ChangeCountersAction(final MagicPermanent permanent, final MagicCounterType counterType, final int amount, final boolean hasScore) {
        this.permanent=permanent;
        this.card=MagicCard.NONE;
        this.counterType=counterType;

        // number of counters cannot become negative
        this.amount = (permanent.getCounters(counterType) + amount >= 0) ?
            amount : -permanent.getCounters(counterType);

        this.hasScore=hasScore;
    }

    private ChangeCountersAction(final MagicCard card, final MagicCounterType counterType, final int amount, final boolean hasScore) {
        this.permanent=MagicPermanent.NONE;
        this.card=card;
        this.counterType=counterType;

        // number of counters cannot become negative
        this.amount = (card.getCounters(counterType) + amount >= 0) ?
            amount : -card.getCounters(counterType);

        this.hasScore=hasScore;
    }

    public static ChangeCountersAction Enters(final MagicPermanent permanent, final MagicCounterType counterType, final int amount) {
        return new ChangeCountersAction(permanent, counterType, amount, false);
    }

    public ChangeCountersAction(final MagicPermanent permanent, final MagicCounterType counterType, final int amount) {
        this(permanent, counterType, amount, true);
    }

    public ChangeCountersAction(final MagicCard card, final MagicCounterType counterType, final int amount) {
        this(card, counterType, amount, true);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (amount == 0) {
            return;
        }
        if (card == MagicCard.NONE) {
            final int oldScore=hasScore?permanent.getScore():0;
            permanent.changeCounters(counterType, amount);
            if (hasScore) {
                setScore(permanent.getController(), permanent.getScore() - oldScore);
            }
        }
        if (permanent == MagicPermanent.NONE) {
            card.changeCounters(counterType, amount);
        }
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (card == MagicCard.NONE) {
            permanent.changeCounters(counterType, -amount);
        }
        if (permanent == MagicPermanent.NONE) {
            card.changeCounters(counterType, -amount);
        }
    }
}
