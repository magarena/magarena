package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;

public class MagicRavnicaLandTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicRavnicaLandTrigger INSTANCE = new MagicRavnicaLandTrigger();
    
    private MagicRavnicaLandTrigger() {}

    public static MagicRavnicaLandTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
        if (player.getLife() < 2) {
            game.doAction(new MagicTapAction(permanent,false));
            return MagicEvent.NONE;
        } else {
            return new MagicEvent(
                permanent,
                player,
                new MagicMayChoice(),
                this,
                "You may$ pay 2 life. If you don't, "+permanent.getName()+" enters the battlefield tapped."
            );
        }
    }

    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choices) {
        if (MagicMayChoice.isYesChoice(choices[0])) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
        } else {
            game.doAction(new MagicTapAction(event.getPermanent(),false));
        }
    }
    
    @Override
    public boolean usesStack() {
        return false;
    }
}
