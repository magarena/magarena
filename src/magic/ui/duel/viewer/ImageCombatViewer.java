package magic.ui.duel.viewer;

import magic.ui.duel.viewer.info.GameViewerInfo;
import magic.ui.duel.viewer.info.PlayerViewerInfo;
import magic.ui.duel.viewer.info.PermanentViewerInfo;
import magic.ui.IChoiceViewer;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import magic.ui.duel.viewer.info.CardViewerInfo;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class ImageCombatViewer extends JPanel implements IChoiceViewer {

    private final SwingGameController controller;
    private final ImagePermanentsViewer permanentsViewer;

    public ImageCombatViewer(final SwingGameController aController) {
        controller = aController;
        controller.registerChoiceViewer(this);

        setLayout(new BorderLayout(6,0));
        setOpaque(false);

        final JPanel leftPanel=new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        add(leftPanel,BorderLayout.WEST);

        if (MagicStyle.getTheme().hasValue(Theme.ICON_SMALL_COMBAT)) {
            final JLabel combatLabel = new JLabel(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_COMBAT));
            combatLabel.setOpaque(true);
            combatLabel.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_ICON_BACKGROUND));
            combatLabel.setPreferredSize(new Dimension(24, 24));
            combatLabel.setBorder(FontsAndBorders.BLACK_BORDER);
            leftPanel.add(combatLabel, BorderLayout.NORTH);
        }

        permanentsViewer=new ImagePermanentsViewer(controller);
        add(permanentsViewer,BorderLayout.CENTER);
    }

    public void update() {
        final SortedSet<PermanentViewerInfo> creatures =
            new TreeSet<>(PermanentViewerInfo.BLOCKED_NAME_COMPARATOR);

        final GameViewerInfo viewerInfo = controller.getViewerInfo();

        final PlayerViewerInfo attackingPlayerInfo=viewerInfo.getAttackingPlayerInfo();
        for (final PermanentViewerInfo permanentInfo : attackingPlayerInfo.permanents) {
            if (permanentInfo.attacking) {
                creatures.add(permanentInfo);
            }
        }

        //add in blockers whose attacker is destroyed
        final PlayerViewerInfo defendingPlayerInfo=viewerInfo.getDefendingPlayerInfo();
        for (final PermanentViewerInfo permanentInfo : defendingPlayerInfo.permanents) {
            if (permanentInfo.blocking && permanentInfo.blockingInvalid) {
                creatures.add(permanentInfo);
            }
        }

        permanentsViewer.viewPermanents(creatures);
    }

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        permanentsViewer.showValidChoices(validChoices);
    }

    public boolean highlightCard(CardViewerInfo cardInfo, boolean b) {
        final ImagePermanentViewer viewer = permanentsViewer.getViewer(cardInfo);
        permanentsViewer.highlightCard(viewer, b ? cardInfo.getId() : 0);
        return viewer != null;
    }
}
