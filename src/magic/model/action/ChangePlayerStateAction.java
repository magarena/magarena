package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;

public class ChangePlayerStateAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicPlayerState state;
    private boolean setPlayer = false;

    public ChangePlayerStateAction(final MagicPlayer aPlayer,final MagicPlayerState aState) {
        player = aPlayer;
        state = aState;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (!player.hasState(state)) {
            player.setState(state);
            setPlayer = true;
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (setPlayer) {
            player.clearState(state);
        }
    }
}
