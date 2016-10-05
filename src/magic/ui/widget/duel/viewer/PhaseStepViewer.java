package magic.ui.widget.duel.viewer;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.model.phase.MagicPhaseType;
import magic.translate.UiString;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PhaseStepViewer extends JPanel {

    // translatable strings
    private static final String _S1 = "Beginning Phase : Untap Step";
    private static final String _S2 = "Beginning Phase : Upkeep Step";
    private static final String _S3 = "Beginning Phase : Draw Step";
    private static final String _S4 = "First Main Phase";
    private static final String _S5 = "Combat Phase : Beginning of Combat Step";
    private static final String _S6 = "Combat Phase : Declare Attackers Step";
    private static final String _S7 = "Combat Phase : Declare Blockers Step";
    private static final String _S8 = "Combat Phase : Combat Damage Step";
    private static final String _S9 = "Combat Phase : End of Combat Step";
    private static final String _S10 = "Second Main Phase";
    private static final String _S11 = "Ending Phase : End of Turn Step";
    private static final String _S12 = "Ending Phase : Clean Up Step";

    private static final Color COLOR_ON = MagicStyle.getRolloverColor();
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
        setPhaseStepIndicator("UN", UiString.get(_S1));
        setPhaseStepIndicator("UP", UiString.get(_S2));
        setPhaseStepIndicator("DR", UiString.get(_S3));
        setPhaseStepIndicator("M1", UiString.get(_S4));
        setPhaseStepIndicator("BC", UiString.get(_S5));
        setPhaseStepIndicator("DA", UiString.get(_S6));
        setPhaseStepIndicator("DB", UiString.get(_S7));
        setPhaseStepIndicator("CD", UiString.get(_S8));
        setPhaseStepIndicator("EC", UiString.get(_S9));
        setPhaseStepIndicator("M2", UiString.get(_S10));
        setPhaseStepIndicator("ET", UiString.get(_S11));
        setPhaseStepIndicator("CU", UiString.get(_S12));
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
