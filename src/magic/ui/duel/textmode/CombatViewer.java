package magic.ui.duel.textmode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.border.Border;
import magic.ui.SwingGameController;
import magic.ui.duel.PermanentViewerInfo;
import magic.ui.duel.PlayerViewerInfo;
import magic.ui.widget.FontsAndBorders;

class CombatViewer extends PermanentsViewer {

    private static final long serialVersionUID = 1L;

    CombatViewer(final SwingGameController controller) {
        super(controller);
        update();
    }

    @Override
    public String getTitle() {
        return "Combat : " + controller.getViewerInfo().getAttackingPlayerInfo().name;
    }

    @Override
    public Collection<PermanentViewerInfo> getPermanents() {
        final PlayerViewerInfo attackingPlayerInfo = controller.getViewerInfo().getAttackingPlayerInfo();
        final PlayerViewerInfo defendingPlayerInfo = controller.getViewerInfo().getDefendingPlayerInfo();
        final SortedSet<PermanentViewerInfo> creatures=new TreeSet<>(PermanentViewerInfo.NAME_COMPARATOR);

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

        final Collection<PermanentViewerInfo> permanents=new ArrayList<>();
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
