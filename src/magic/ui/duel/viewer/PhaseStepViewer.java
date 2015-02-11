package magic.ui.duel.viewer;

import magic.model.phase.MagicPhaseType;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import magic.ui.MagicStyle;

@SuppressWarnings("serial")
public class PhaseStepViewer extends JPanel {

    private int currentPhaseStep = -1;

    // CTR
    public PhaseStepViewer() {
        setMyLayout();
    }

    private void setMyLayout() {
        setLayout(new MigLayout("insets 1 5 0 5, gap 0"));
        setPhaseStepIndicator("UN", "Beginning Phase : Untap Step");
        setPhaseStepIndicator("UP", "Beginning Phase : Upkeep Step");
        setPhaseStepIndicator("DR", "Beginning Phase : Draw Step");
        setPhaseStepIndicator("M1", "First Main Phase");
        setPhaseStepIndicator("BC", "Combat Phase : Beginning of Combat Step");
        setPhaseStepIndicator("DA", "Combat Phase : Declare Attackers Step");
        setPhaseStepIndicator("DB", "Combat Phase : Declare Blockers Step");
        setPhaseStepIndicator("CD", "Combat Phase : Combat Damage Step");
        setPhaseStepIndicator("EC", "Combat Phase : End of Combat Step");
        setPhaseStepIndicator("M2", "Second Main Phase");
        setPhaseStepIndicator("ET", "Ending Phase : End of Turn Step");
        setPhaseStepIndicator("CU", "Ending Phase : Clean Up Step");
    }

    private void setPhaseStep(int index) {
        if (index != currentPhaseStep) {
            JLabel lbl;
            if (index != -1) {
                lbl = (JLabel)getComponent(index);
                lbl.setForeground(MagicStyle.getTheme().getTextColor());
            }
            if (currentPhaseStep != -1) {
                lbl = (JLabel)getComponent(currentPhaseStep);
                lbl.setForeground(Color.GRAY);
            }
            currentPhaseStep = index;
        }
    }

    private void setPhaseStepIndicator(String caption, String tooltip) {
        JLabel lbl = new JLabel(caption);
        lbl.setOpaque(false);
        lbl.setToolTipText(tooltip);
        lbl.setFont(FontsAndBorders.FONT1);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setForeground(Color.GRAY);
        add(lbl, "w 10:100%");
    }

    public void setPhaseStep(MagicPhaseType gamePhaseType) {
        setPhaseStep(gamePhaseType.ordinal() - 1);
    }
}
