package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.phase.MagicPhaseType;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class MagicChangePlayerStateAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicPlayerState state;

    public MagicChangePlayerStateAction(final MagicPlayer aPlayer,final MagicPlayerState aState) {
        player = aPlayer;
        state = aState;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        if (player.hasState(state)) {
            // do nothing
        } else if (state == MagicPlayerState.Exhausted) {
            // no duration, manually removed
            game.doAction(new MagicAddStaticAction(new MagicStatic(MagicLayer.Player) {
                @Override
                public void modPlayer(
                        final MagicPermanent source,
                        final MagicPlayer aPlayer) {
                    if (player.getId() == aPlayer.getId()) {
                        // remove Exhausted state during Untap as MagicUntapPhase 
                        // stores a copy of the state at begin step of untap phase
                        if (aPlayer.getGame().isPhase(MagicPhaseType.Untap)) {
                            aPlayer.getGame().addDelayedAction(new MagicRemoveStaticAction(this));
                        } else {
                            aPlayer.setState(state);
                        }
                    }
                }   
            }));
        } else {
            // until end of turn
            game.doAction(new MagicAddStaticAction(new MagicStatic(MagicLayer.Player, MagicStatic.UntilEOT) {
                @Override
                public void modPlayer(
                        final MagicPermanent source,
                        final MagicPlayer aPlayer) {
                    if (player.getId() == aPlayer.getId()) {
                        aPlayer.setState(state);
                    }
                }   
            }));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {}    
}
