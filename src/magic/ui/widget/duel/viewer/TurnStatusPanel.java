package magic.ui.widget.duel.viewer;

import javax.swing.JPanel;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TurnStatusPanel extends JPanel {

    private final MigLayout miglayout = new MigLayout();
    private final TurnTitlePanel turnTitlePanel;
    private final PhaseStepViewer phaseStepViewer = new PhaseStepViewer();

    public TurnStatusPanel(final SwingGameController controller) {
        this.turnTitlePanel = new TurnTitlePanel(controller);
        setLookAndFeel();
        setLayout(miglayout);
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(true);
        setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        //
        phaseStepViewer.setOpaque(false);
    }

    private void refreshLayout() {
        miglayout.setLayoutConstraints("insets 3 2 2 2, gap 0, flowy");
        miglayout.setColumnConstraints("fill");
        removeAll();
        add(turnTitlePanel);
        add(phaseStepViewer, "aligny bottom, pushy");
    }

    public void refresh(final GameViewerInfo gameInfo) {
        turnTitlePanel.refresh(gameInfo);
        phaseStepViewer.setPhaseStep(gameInfo.getPhaseType());
    }

}
