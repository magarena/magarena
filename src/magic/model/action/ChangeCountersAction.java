package magic.model.action;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.trigger.MagicCounterChangeTriggerData;
import magic.model.trigger.MagicTriggerType;

public class ChangeCountersAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicObject obj;
    private final MagicCounterType counterType;
    private int amount;
    private final boolean hasScore;

    private ChangeCountersAction(final MagicPlayer player, final MagicObject obj, final MagicCounterType counterType, final int amount, final boolean hasScore) {
        this.player = player;
        this.obj=obj;
        this.counterType=counterType;

        // number of counters cannot become negative
        this.amount = (obj.getCounters(counterType) + amount >= 0) ?
            amount : -obj.getCounters(counterType);

        this.hasScore=hasScore;
    }

    public static ChangeCountersAction Enters(final MagicPermanent permanent, final MagicCounterType counterType, final int amount) {
        return new ChangeCountersAction(permanent.getController(), permanent, counterType, amount, false);
    }

    public ChangeCountersAction(final MagicPlayer player, final MagicObject obj, final MagicCounterType counterType, final int amount) {
        this(player, obj, counterType, amount, true);
    }

    public MagicPlayer getPlayer() {
        return player;
    }

    public MagicObject getObj() {
        return obj;
    }

    public MagicCounterType getCounterType() {
        return counterType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int aAmount) {
        amount = aAmount;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.executeTrigger(MagicTriggerType.IfCounterWouldChange, this);

        if (amount == 0) {
            return;
        }

        final int oldScore = hasScore && obj.isPermanent() ? ((MagicPermanent)obj).getScore() : 0;

        obj.changeCounters(counterType, amount);
        if (hasScore && obj.isPermanent()) {
            setScore(obj.getController(), ((MagicPermanent)obj).getScore() - oldScore);
        }

        final MagicCounterChangeTriggerData data = new MagicCounterChangeTriggerData(player, obj, counterType, amount);
        if (amount != 0) {
            game.executeTrigger(MagicTriggerType.WhenOneOrMoreCountersAreChanged, data);
        }
        for (int i = 0; i < Math.abs(amount); i++) {
            game.executeTrigger(MagicTriggerType.WhenACounterIsChanged, data);
        }
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        obj.changeCounters(counterType, -amount);
    }
}
