package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;

public class BecomeMonarchAction extends ChangePlayerStateAction {

    private final MagicPlayer player;

    public BecomeMonarchAction(MagicPlayer aPlayer) {
        super(aPlayer, MagicPlayerState.Monarch);
        player=aPlayer;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (!player.isMonarch()) {
            if (player.getOpponent().isMonarch()) {
                player.getOpponent().clearState(MagicPlayerState.Monarch);
            }
            player.setState(MagicPlayerState.Monarch);
            System.out.println("Player becomes monarch");
        }
    }
}
