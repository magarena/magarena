package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class MagicExaltedTrigger extends MagicWhenAttacksTrigger {

    private static final MagicExaltedTrigger INSTANCE = new MagicExaltedTrigger();

    private MagicExaltedTrigger() {}

    public static MagicExaltedTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
        return (permanent.isFriend(creature) &&
                permanent.getController().getNrOfAttackers()==1) ?
            new MagicEvent(
                permanent,
                creature,
                this,
                "RN gets +1/+1 until end of turn."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeTurnPTAction(event.getRefPermanent(),1,1));
    }
}
