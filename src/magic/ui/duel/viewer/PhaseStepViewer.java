package magic.ui.duel.viewer;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.model.phase.MagicPhaseType;
import magic.ui.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PhaseStepViewer extends JPanel {

    private static final Color COLOR_ON = MagicStyle.HIGHLIGHT_COLOR;
    private static final Color COLOR_OFF = Color.LIGHT_GRAY;
    private static final Font FONT_ON = new Font("dialog", Font.BOLD, 12);
    private static final Font FONT_OFF = new Font("dialog", Font.PLAIN, 12);

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
                lbl.setForeground(COLOR_ON);
                lbl.setFont(FONT_ON);
            }
            if (currentPhaseStep != -1) {
                lbl = (JLabel)getComponent(currentPhaseStep);
                lbl.setForeground(COLOR_OFF);
                lbl.setFont(FONT_OFF);
            }
            currentPhaseStep = index;
        }
    }

    private void setPhaseStepIndicator(String caption, String tooltip) {
        JLabel lbl = new JLabel(caption);
        lbl.setOpaque(false);
        lbl.setToolTipText(tooltip);
        lbl.setFont(FONT_OFF);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setForeground(COLOR_OFF);
        add(lbl, "w 10:100%");
    }

    public void setPhaseStep(MagicPhaseType gamePhaseType) {
        setPhaseStep(gamePhaseType.ordinal() - 1);
    }
}
