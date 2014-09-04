package magic.ui.viewer;

import magic.ui.GameController;
import magic.ui.widget.FontsAndBorders;

import javax.swing.border.Border;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class CombatViewer extends PermanentsViewer {

    private static final long serialVersionUID = 1L;

    public CombatViewer(final GameController controller) {
        super(controller.getViewerInfo(), controller);
        update();
    }

    @Override
    public String getTitle() {
        return "Combat : "+viewerInfo.getAttackingPlayerInfo().name;
    }

    @Override
    public Collection<PermanentViewerInfo> getPermanents() {
        final PlayerViewerInfo attackingPlayerInfo=viewerInfo.getAttackingPlayerInfo();
        final PlayerViewerInfo defendingPlayerInfo=viewerInfo.getDefendingPlayerInfo();
        final SortedSet<PermanentViewerInfo> creatures=new TreeSet<PermanentViewerInfo>(PermanentViewerInfo.NAME_COMPARATOR);

        for (final PermanentViewerInfo permanentInfo : attackingPlayerInfo.permanents) {
            if (permanentInfo.attacking) {
                creatures.add(permanentInfo);
            }
        }

        for (final PermanentViewerInfo permanentInfo : defendingPlayerInfo.permanents) {
            if (permanentInfo.blocking&&permanentInfo.blockingInvalid) {
                creatures.add(permanentInfo);
            }
        }

        final Collection<PermanentViewerInfo> permanents=new ArrayList<PermanentViewerInfo>();
        for (final PermanentViewerInfo creature : creatures) {
            permanents.add(creature);
            permanents.addAll(creature.blockers);
        }
        return permanents;
    }

    @Override
    public boolean isSeparated(final PermanentViewerInfo permanentInfo1,final PermanentViewerInfo permanentInfo2) {
        if (permanentInfo2.attacking) {
            return true;
        }
        return permanentInfo2.blockingInvalid;
    }

    @Override
    public Border getBorder(final PermanentViewerInfo permanentInfo) {
        return FontsAndBorders.getPlayerBorder(permanentInfo.visible);
    }
}
