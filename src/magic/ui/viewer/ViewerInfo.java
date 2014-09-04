package magic.ui.viewer;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.stack.MagicItemOnStack;

import java.util.ArrayList;
import java.util.List;

public class ViewerInfo {

    private PlayerViewerInfo playerInfo;
    private PlayerViewerInfo opponentInfo;
    private List<StackViewerInfo> stack;

    public ViewerInfo(final MagicGame game) {
        update(game);
    }

    public synchronized PlayerViewerInfo getPlayerInfo(final boolean opponent) {
        return opponent?opponentInfo:playerInfo;
    }

    public synchronized PlayerViewerInfo getAttackingPlayerInfo() {
        return playerInfo.turn?playerInfo:opponentInfo;
    }

    public synchronized PlayerViewerInfo getDefendingPlayerInfo() {
        return playerInfo.turn?opponentInfo:playerInfo;
    }

    public synchronized boolean isVisiblePlayer(final MagicPlayer player) {
        return playerInfo.player==player;
    }

    public synchronized List<StackViewerInfo> getStack() {
        return stack;
    }

    public final synchronized void update(final MagicGame game) {
        final MagicPlayer player=game.getVisiblePlayer();
        playerInfo=new PlayerViewerInfo(game,player);
        opponentInfo=new PlayerViewerInfo(game,player.getOpponent());

        stack=new ArrayList<StackViewerInfo>();
        for (final MagicItemOnStack itemOnStack : game.getStack()) {
            stack.add(new StackViewerInfo(game,itemOnStack));
        }
    }
}
