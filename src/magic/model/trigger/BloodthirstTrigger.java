package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayerState;
import magic.model.action.ChangeCountersAction;
import magic.model.event.MagicEvent;

public class BloodthirstTrigger extends EntersBattlefieldTrigger {

    private final int amount;

    public BloodthirstTrigger(final int aAmount) {
        super(MagicTrigger.REPLACEMENT);
        amount = aAmount;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return (permanent.getOpponent().hasState(MagicPlayerState.WasDealtDamage)) ?
            new MagicEvent(
                permanent,
                this,
                amount > 1 ?
                    "SN enters the battlefield with " +  amount + " +1/+1 counters on it." :
                    "SN enters the battlefield with a +1/+1 counter on it."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(ChangeCountersAction.Enters(
            event.getPermanent(),
            MagicCounterType.PlusOne,
            amount
        ));
    }
}

