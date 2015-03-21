/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magic.ui.duel.viewer;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import magic.model.MagicGame;
import magic.ui.SwingGameController;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TurnStatusPanel extends JPanel {

    private final MigLayout miglayout = new MigLayout("insets 0, gap 0, flowy");
    private final TurnTitlePanel turnTitlePanel;
    private final PhaseStepViewer phaseStepViewer = new PhaseStepViewer();
    private final UserActionPanel userActionPanel;

    public TurnStatusPanel(final UserActionPanel userActionPanel, final SwingGameController controller) {
        this.userActionPanel = userActionPanel;
        this.turnTitlePanel = new TurnTitlePanel(userActionPanel, controller);
        setLookAndFeel();
        setLayout(miglayout);
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        //
        phaseStepViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        phaseStepViewer.setOpaque(false);
    }

    private void refreshLayout() {
        removeAll();
        add(turnTitlePanel, "w 100%");
        add(phaseStepViewer, "w 100%");
        add(userActionPanel, "w 100%, h 100%");
    }

    public void refresh(final MagicGame game) {
        turnTitlePanel.refresh(game);
        phaseStepViewer.setPhaseStep(userActionPanel.getMagicPhaseType());
    }

}
