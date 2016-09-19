package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;

public class BecomeMonarchAction extends MagicAction {

    private final MagicPlayer player;
    private boolean setPlayer = false;
    private boolean clearOpponent = false;

    public BecomeMonarchAction(final MagicPlayer aPlayer) {
        player = aPlayer;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (!player.isMonarch()) {
            if (player.getOpponent().isMonarch()) {
                player.getOpponent().clearState(MagicPlayerState.Monarch);
                clearOpponent = true;
            }
            player.setState(MagicPlayerState.Monarch);
            setPlayer = true;
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (setPlayer) {
            player.clearState(MagicPlayerState.Monarch);
        }
        if (clearOpponent) {
            player.getOpponent().setState(MagicPlayerState.Monarch);
        }
    }
}
