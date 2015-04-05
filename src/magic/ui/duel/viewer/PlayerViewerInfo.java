package magic.ui.duel.viewer;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;

import java.util.ArrayList;
import java.util.List;

public class PlayerViewerInfo {

    public final MagicPlayer player;
    public final boolean turn;
    public final String name;
    public final int life;
    public final int poison;
    public final int preventDamage;
    public final int lands;
    public final MagicCardList hand;
    public final MagicCardList graveyard;
    public final MagicCardList exile;
    public final MagicCardList library;
    public final List<PermanentViewerInfo> permanents;
    public final boolean isAi;
    public final String playerLabel;

    public PlayerViewerInfo(final MagicGame game,final MagicPlayer player) {
        this.player=player;
        turn=player==game.getTurnPlayer();
        name=player.getName();
        playerLabel=player.getPlayerDefinition().getPlayerProfile().getPlayerLabel();
        life=player.getLife();
        poison=player.getPoison();
        preventDamage=player.getPreventDamage();
        lands=player.getNrOfPermanents(MagicType.Land);
        hand=new MagicCardList(player.getHand());
        graveyard=new MagicCardList(player.getGraveyard());
        exile=new MagicCardList(player.getExile());
        library=new MagicCardList(player.getLibrary());
        isAi = player.isArtificial();
        permanents=new ArrayList<PermanentViewerInfo>();
        for (final MagicPermanent permanent : player.getPermanents()) {
            permanents.add(new PermanentViewerInfo(game,permanent));
        }
    }
}
