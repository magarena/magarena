package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class GainControlAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicPermanent permanent;
    private final boolean duration;

    public GainControlAction(final MagicPlayer player,final MagicPermanent permanent) {
        this(player,permanent,MagicStatic.Forever);
    }

    public GainControlAction(final MagicPlayer player,final MagicPermanent permanent,final boolean duration) {
        this.player = player;
        this.permanent = permanent;
        this.duration = duration;
    }

    @Override
    public void doAction(final MagicGame game) {
        //insert continuous effect
        game.doAction(new AddStaticAction(
            permanent,
            new MagicStatic(MagicLayer.Control, duration) {
                @Override
                public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer controller) {
                    final MagicGame game = controller.getGame();
                    return game.getPlayer(player.getIndex());
                }
            }
        ));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }

    @Override
    public String toString() {
        return super.toString() + " (" + player + "," + permanent + ')';
    }
}
