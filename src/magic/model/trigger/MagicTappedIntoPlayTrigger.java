package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicTappedIntoPlayTrigger INSTANCE = new MagicTappedIntoPlayTrigger();

    private MagicTappedIntoPlayTrigger() {}

    public static MagicTappedIntoPlayTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
        return new MagicEvent(
            permanent,
            this,
            "SN enters the battlefield tapped."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicTapAction(event.getPermanent(),false));
    }
    
    @Override
    public boolean usesStack() {
        return false;
    }
}
