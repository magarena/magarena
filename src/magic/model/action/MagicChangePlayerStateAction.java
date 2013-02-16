package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPlayerState;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class MagicChangePlayerStateAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicPlayerState state;
    private final boolean set;
    private boolean changed;

    public MagicChangePlayerStateAction(final MagicPlayer player,final MagicPlayerState state,final boolean set) {
        this.player=player;
        this.state=state;
        this.set=set;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        changed = player.hasState(state) != set;

        // special case for Exhaustion state
        if (state == MagicPlayerState.Exhausted) {
            if (set) {
                player.setState(state);
            } else {
                player.clearState(state);
            }
            return;
        }

        // all other states handled by continuous effect layer
        game.doAction(new MagicAddStaticAction(new MagicStatic(
                MagicLayer.Player,
                MagicStatic.UntilEOT) {
            @Override
            public void modPlayer(
                    final MagicPermanent source,
                    final MagicPlayer aPlayer) {
                if (player.getId() == aPlayer.getId()) {
                    if (set) {
                        aPlayer.setState(state);
                    } else {
                        aPlayer.clearState(state);
                    }
                }
            }   
        }));
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (state == MagicPlayerState.Exhausted && changed) {
            if (set) {
                player.clearState(state);
            } else {
                player.setState(state);
            }
        }
    }    
}
