package magic.game.state;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private int difficulty;
    private final List<GamePlayerState> players = new ArrayList<>();
    private int startPlayerNumber = 0;

    public void setDifficulty(int i) {
        this.difficulty = i;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public GamePlayerState getPlayer(int i) {
        try {
            return players.get(i);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            players.add(new GamePlayerState());
            return players.get(i);
        }
    }

    public int getStartPlayerIndex() {
        return startPlayerNumber;
    }

    public void setStartPlayerIndex(int i) {
        this.startPlayerNumber = i;
    }

    public List<GamePlayerState> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        final String LF = "\n";
        final StringBuilder sb = new StringBuilder();
        sb.append("# GAME STATE").append(LF);
        sb.append("difficulty=").append(getDifficulty()).append(LF);
        sb.append("startPlayer=").append(getStartPlayerIndex()).append(LF);
        for (int i = 0; i < players.size(); i++) {
            sb.append("PLAYER ").append(i).append(" STATE").append(LF);
            final GamePlayerState player = getPlayers().get(i);
            sb.append("name=").append(player.getName()).append(LF);
            sb.append("life=").append(player.getLife()).append(LF);
            sb.append("face=").append(player.getFace()).append(LF);
            sb.append("ai=").append(player.getAiType()).append(LF);
            sb.append("deck.color=").append(player.getDeckProfileColors()).append(LF);
            // HAND
            sb.append("PLAYER ").append(i).append(" HAND").append(LF);
            for (GameCardState card : players.get(i).getHand()) {
                sb.append(card.getCardName()).append(" x").append(card.getQuantity()).append(LF);
            }
            //  PERMANENTS
            sb.append("PLAYER ").append(i).append(" PERMANENTS").append(LF);
            for (GameCardState card : players.get(i).getPermanents()) {
                sb.append(card.getCardName()).append(" x").append(card.getQuantity()).append(LF);
            }
            //  LIBRARY
            sb.append("PLAYER ").append(i).append(" LIBRARY").append(LF);
            for (GameCardState card : players.get(i).getLibrary()) {
                sb.append(card.getCardName()).append(" x").append(card.getQuantity()).append(LF);
            }
        }
        return sb.toString();
    }

}
