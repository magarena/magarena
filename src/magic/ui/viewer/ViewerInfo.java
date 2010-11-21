package magic.ui.viewer;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.stack.MagicItemOnStack;

public class ViewerInfo {

	private PlayerViewerInfo playerInfo;
	private PlayerViewerInfo opponentInfo;
	private List<StackViewerInfo> stack;
			
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
	
	public synchronized void update(final MagicGame game) {

		final MagicPlayer player=game.getVisiblePlayer();
		playerInfo=new PlayerViewerInfo(game,player);
		opponentInfo=new PlayerViewerInfo(game,game.getOpponent(player));

		stack=new ArrayList<StackViewerInfo>();
		for (final MagicItemOnStack itemOnStack : game.getStack()) {
		
			stack.add(new StackViewerInfo(game,itemOnStack));
		}
	}
}