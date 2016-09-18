package magic.ui.duel.viewer.info;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.ui.MagicImages;

public class PlayerViewerInfo {

    public final MagicPlayer player;
    private final boolean isPlayerTurn;
    private final String name;
    public final int life;
    public final int poison;
    public final int preventDamage;
    public final int lands;
    public final MagicCardList hand;
    public final MagicCardList graveyard;
    public final MagicCardList exile;
    public final MagicCardList library;
    public final List<PermanentViewerInfo> permanents;
    private final boolean isAi;
    private final boolean isMonarch;
    public final String playerLabel;
    private final ImageIcon avatar;
    private int gamesWon;

    PlayerViewerInfo(final MagicGame game, final MagicPlayer player) {
        this.player = player;
        isPlayerTurn = player == game.getTurnPlayer();
        name = player.getName();
        playerLabel = player.getPlayerDefinition().getProfile().getPlayerLabel();
        life = player.getLife();
        poison = player.getPoison();
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
        avatar = MagicImages.getIconSize4(player.getPlayerDefinition());
    }

    public ImageIcon getAvatar() {
        return this.avatar;
    }

    public boolean isAi() {
        return this.isAi;
    }

    public boolean isMonarch() {
        return isMonarch;
    }

    public boolean isHuman() {
        return !isAi();
    }

    public boolean isPlayerTurn() {
        return this.isPlayerTurn;
    }

    public int getGamesWon() {
        return this.gamesWon;
    }

    void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public String getName() {
        return this.name;
    }

}
