package magic.ui.duel.viewer;

import magic.ui.screen.duel.game.PermanentFilter;
import magic.ui.IUpdatable;
import magic.ui.IChoiceViewer;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Set;
import magic.ui.duel.viewer.info.CardViewerInfo;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class ImageBattlefieldViewer extends JPanel implements IChoiceViewer, IUpdatable {

    private final SwingGameController controller;
    private final boolean opponent;
    private final ImagePermanentsViewer permanentsViewer;
    private final PermanentFilter permanentFilter;

    public ImageBattlefieldViewer(final SwingGameController controller,final boolean opponent) {
        this.controller = controller;
        this.opponent=opponent;

        controller.registerChoiceViewer(this);

        setOpaque(false);

        setLayout(new BorderLayout(6,0));

        final JPanel leftPanel=new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        add(leftPanel,BorderLayout.WEST);

        if (MagicStyle.getTheme().hasValue(Theme.ICON_SMALL_BATTLEFIELD)) {
            final JLabel iconLabel = new JLabel(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_BATTLEFIELD));
            iconLabel.setOpaque(true);
            iconLabel.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_ICON_BACKGROUND));
            iconLabel.setPreferredSize(new Dimension(24, 24));
            iconLabel.setBorder(FontsAndBorders.BLACK_BORDER);
            leftPanel.add(iconLabel, BorderLayout.NORTH);
        }

        permanentsViewer=new ImagePermanentsViewer(controller, opponent);
        add(permanentsViewer,BorderLayout.CENTER);

        permanentFilter=new PermanentFilter();
    }

    @Override
    public void update() {
        permanentsViewer.viewPermanents(permanentFilter.getPermanents(controller.getViewerInfo(), opponent));
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
