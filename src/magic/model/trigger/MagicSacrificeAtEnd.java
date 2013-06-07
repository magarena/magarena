package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;

public class MagicSacrificeAtEnd extends MagicAtEndOfTurnTrigger {

    private static final MagicSacrificeAtEnd INSTANCE = new MagicSacrificeAtEnd();

    private MagicSacrificeAtEnd() {}

    public static MagicSacrificeAtEnd create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
        final MagicPlayer player = permanent.getController();
        return (player == eotPlayer) ?
            new MagicEvent(
                permanent,
                player,
                this,
                "Sacrifice SN."):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicSacrificeAction(event.getPermanent()));
    }
}
