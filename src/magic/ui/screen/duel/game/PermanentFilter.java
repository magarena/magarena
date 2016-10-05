package magic.ui.screen.duel.game;

import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.duel.viewerinfo.PermanentViewerInfo;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PermanentFilter {

    private static final Comparator<PermanentViewerInfo> PERMANENT_COMPARATOR=new Comparator<PermanentViewerInfo>() {

        @Override
        public int compare(final PermanentViewerInfo permanentInfo1,final PermanentViewerInfo permanentInfo2) {

            final int positionDif=permanentInfo1.position-permanentInfo2.position;
            if (positionDif!=0) {
                return positionDif;
            }

            final int nameDif=permanentInfo1.name.compareTo(permanentInfo2.name);
            if (nameDif!=0) {
                return nameDif;
            }

            return permanentInfo1.permanent.compareTo(permanentInfo2.permanent);
        }
    };

    private boolean accept(final PermanentViewerInfo permanentInfo) {
        if (permanentInfo.attacking || permanentInfo.blocking) {
            return false;
        }
        return permanentInfo.root;
    }

    public SortedSet<PermanentViewerInfo> getPermanents(final GameViewerInfo viewerInfo, final boolean opponent) {
        final PlayerViewerInfo player = viewerInfo.getPlayerInfo(opponent);
        final SortedSet<PermanentViewerInfo> permanents = new TreeSet<>(PERMANENT_COMPARATOR);
        for (final PermanentViewerInfo permanentInfo : player.permanents) {
            if (accept(permanentInfo)) {
                permanents.add(permanentInfo);
            }
        }
        return permanents;
    }

}
