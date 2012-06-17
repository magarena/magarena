package magic.model.action;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class MagicChangeCountersAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicCounterType counterType;
    private final int amount;
    private final boolean hasScore;
    
    public MagicChangeCountersAction(
            final MagicPermanent permanent,
            final MagicCounterType counterType,
            final int amount,
            final boolean hasScore) {
        this.permanent=permanent;
        this.counterType=counterType;
        this.amount=amount;
        this.hasScore=hasScore;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        final int oldScore=hasScore?permanent.getScore():0;
        permanent.changeCounters(counterType,amount);
        if (hasScore) {
            setScore(permanent.getController(),permanent.getScore()-oldScore);
        }
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        permanent.changeCounters(counterType,-amount);
    }
}
