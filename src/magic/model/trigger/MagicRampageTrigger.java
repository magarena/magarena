package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.ChangeTurnPTAction;
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
                amount,
                this,
                "SN gets +" + amount + "/+" +  amount + " until end of turn."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final int amount = event.getRefInt();
        game.doAction(new ChangeTurnPTAction(
            event.getPermanent(),
            amount,
            amount
        ));
    }
}
