package magic.model.trigger;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicMessage;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.ReturnLinkedExileAction;
import magic.model.event.MagicEvent;

public class LeavesReturnExiledTrigger extends LeavesBattlefieldTrigger {

    private static final LeavesReturnExiledTrigger INSTANCE = new LeavesReturnExiledTrigger();

    private LeavesReturnExiledTrigger() {}

    public static final LeavesReturnExiledTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
        if (act.isPermanent(permanent) &&
            !permanent.getExiledCards().isEmpty()) {
            final MagicCardList clist = new MagicCardList(permanent.getExiledCards());
            return new MagicEvent(
                permanent,
                this,
                "Return " + MagicMessage.getTokenizedCardNames(clist) + " to the battlefield."
            );
        }
        return MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ReturnLinkedExileAction(event.getPermanent(),MagicLocationType.Battlefield));
    }
}
