package magic.ui.duel.viewerinfo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardList;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.ui.MagicImages;

public class PlayerViewerInfo {

    public final MagicPlayer player;
    private final int playerIndex;
    private final boolean isPlayerTurn;
    private final String name;
    public final int life;
    public final int poison;
    public final int energy;
    public final int experience;
    public final int preventDamage;
    public final int lands;
    private final String deckName;
    public final MagicCardList hand;
    public final MagicCardList graveyard;
    public final MagicCardList exile;
    public final MagicCardList library;
    public final List<PermanentViewerInfo> permanents;
    private final boolean isAi;
    private final boolean isMonarch;
    public final String playerLabel;
    private int gamesWon;
    private final boolean hasPriority;
    private final boolean isWinner;
    private final ImageIcon newTurnAvatar;
    private final ImageIcon playerPanelAvatar;

    PlayerViewerInfo(final MagicGame game, final MagicPlayer player) {
        this.player = player;
        isWinner = game.getWinner() == player;
        hasPriority = game.getPriorityPlayer() == player;
        playerIndex = player.getIndex();
        deckName = player.getConfig().getDeck().getQualifiedName();
        isPlayerTurn = player == game.getTurnPlayer();
        name = player.getName();
        playerLabel = player.getConfig().getProfile().getPlayerLabel();
        life = player.getLife();
        poison = player.getCounters(MagicCounterType.Poison);
        energy = player.getCounters(MagicCounterType.Energy);
        experience = player.getCounters(MagicCounterType.Experience);
        preventDamage = player.getPreventDamage();
        lands = player.getNrOfPermanents(MagicType.Land);
        hand = new MagicCardList(player.getHand());
        graveyard = new MagicCardList(player.getGraveyard());
        exile = new MagicCardList(player.getExile());
        library = new MagicCardList(player.getLibrary());
        isAi = player.isArtificial();
        isMonarch = player.isMonarch();
        permanents = new ArrayList<>();
        for (final MagicPermanent permanent : player.getPermanents()) {
            permanents.add(new PermanentViewerInfo(game, permanent));
        }
        newTurnAvatar = MagicImages.getIconSize4(this.getConfig());
        playerPanelAvatar =  MagicImages.getIconSize3(this.getConfig());
    }

    /**
     * Avatar image displayed at the start of the player's turn.
     */
    public ImageIcon getNewTurnAvatar() {
        return newTurnAvatar;
    }

    /**
     * Avatar image displayed in game panel.
     */
    public ImageIcon getPlayerPanelAvatar() {
        return playerPanelAvatar;
    }

    public boolean isAi() {
        return isAi;
    }

    public boolean isMonarch() {
        return isMonarch;
    }

    public boolean isHuman() {
        return !isAi;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public String getName() {
        return name;
    }

    public String getQualifiedDeckName() {
        return deckName;
    }

    /**
     * 0 = player, 1 = opponent.
     */
    public int getPlayerIndex() {
        return playerIndex;
    }

    boolean hasPriority() {
        return hasPriority;
    }

    public DuelPlayerConfig getConfig() {
        return player.getConfig();
    }

    boolean isWinner() {
        return isWinner;
    }
}
