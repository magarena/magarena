package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class MagicRampageTrigger extends MagicWhenBecomesBlockedTrigger {
    private final int n;

    public MagicRampageTrigger(final int n) {
        this.n = n;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
        final MagicPermanentList plist = permanent.getBlockingCreatures();
        final int amount = n * (plist.size() - 1);
        return (creature == permanent && amount > 0) ?
            new MagicEvent(
                permanent,
                new Object[]{amount},
                this,
                "SN gets +" + amount + "/+" +  amount + " until end of turn."
            ):
            MagicEvent.NONE;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
        final int amount = (Integer)data[0];
        game.doAction(new MagicChangeTurnPTAction(
            event.getPermanent(),
            amount,
            amount
        ));
    }
}
