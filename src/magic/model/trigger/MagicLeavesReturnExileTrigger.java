package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;

public class MagicLeavesReturnExileTrigger extends MagicWhenLeavesPlayTrigger {

    private static final MagicLeavesReturnExileTrigger INSTANCE = new MagicLeavesReturnExileTrigger();

    private MagicLeavesReturnExileTrigger() {}

    public static final MagicLeavesReturnExileTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
        if (permanent == data &&
            !permanent.getExiledCards().isEmpty()) {
            final MagicCard exiledCard = permanent.getExiledCards().get(0);
            return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{exiledCard,permanent.getController()},
                    this,
                    "Return " + exiledCard + " to the battlefield");
        }
        return MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        final MagicCard exiledCard = (MagicCard)data[0];
        game.doAction(new MagicRemoveCardAction(exiledCard,MagicLocationType.Exile));
        game.doAction(new MagicPlayCardAction(exiledCard,exiledCard.getOwner(),MagicPlayCardAction.NONE));
    }
}
