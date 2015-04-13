package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicTriggerOnStack;

public abstract class MagicTributeTrigger extends MagicWhenComesIntoPlayTrigger {

    private final int amt;

    private final MagicEventAction action = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            if (event.isYes()) {
                game.doAction(new ChangeCountersAction(
                    permanent,
                    MagicCounterType.PlusOne,
                    event.getRefInt()
                ));
            } else {
                game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(getEvent(permanent))));
            }
        }
    };

    public MagicTributeTrigger(final int aAmt) {
        amt = aAmt;
    }
    
    public static MagicTributeTrigger create(final int amt, final MagicSourceEvent sourceEvent) {
        return new MagicTributeTrigger(amt) {
            @Override
            public MagicEvent getEvent(final MagicPermanent permanent) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent perm, final MagicPayedCost payedCost) {
        return new MagicEvent(
            perm,
            perm.getOpponent(),
            new MagicMayChoice(),
            amt,
            action,
            "PN may$ put RN +1/+1 counters on SN."
        );
    }

    @Override
    public boolean usesStack() {
        return false;
    }
    
    public abstract MagicEvent getEvent(final MagicPermanent permanent);
}
