package magic.ui.screen.duel.game;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.duel.viewerinfo.PermanentViewerInfo;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;

public class PermanentFilter {

    private static final Comparator<PermanentViewerInfo> PERMANENT_COMPARATOR= (permanentInfo1, permanentInfo2) -> {
        final int positionDif = permanentInfo1.position - permanentInfo2.position;
        if (positionDif != 0) {
            return positionDif;
        }

        final int nameDif = permanentInfo1.name.compareTo(permanentInfo2.name);
        if (nameDif != 0) {
            return nameDif;
        }

        return permanentInfo1.permanent.compareTo(permanentInfo2.permanent);
    };

    private boolean accept(final PermanentViewerInfo permanentInfo) {
        if (permanentInfo.attacking || permanentInfo.blocking) {
            return false;
        }
        return permanentInfo.root;
    }

    public SortedSet<PermanentViewerInfo> getPermanents(GameViewerInfo gameInfo, boolean isOpponent) {
        PlayerViewerInfo player = isOpponent ? gameInfo.getOpponent() : gameInfo.getMainPlayer();
        SortedSet<PermanentViewerInfo> permanents = new TreeSet<>(PERMANENT_COMPARATOR);
        player.permanents.stream().filter(p -> accept(p))
            .forEach(p -> permanents.add(p));
        return permanents;
    }

}
