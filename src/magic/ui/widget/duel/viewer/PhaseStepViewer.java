package magic.ui.widget.duel.viewer;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.model.phase.MagicPhaseType;
import magic.translate.MText;
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

    // use a LinkedHashMap to retain insertion order.
    private static final Map<MagicPhaseType, String> phasesMap = new LinkedHashMap<>();
    static {
        phasesMap.put(MagicPhaseType.Untap, _S1);
        phasesMap.put(MagicPhaseType.Upkeep, _S2);
        phasesMap.put(MagicPhaseType.Draw, _S3);
        phasesMap.put(MagicPhaseType.FirstMain, _S4);
        phasesMap.put(MagicPhaseType.BeginOfCombat, _S5);
        phasesMap.put(MagicPhaseType.DeclareAttackers, _S6);
        phasesMap.put(MagicPhaseType.DeclareBlockers, _S7);
        phasesMap.put(MagicPhaseType.CombatDamage, _S8);
        phasesMap.put(MagicPhaseType.EndOfCombat, _S9);
        phasesMap.put(MagicPhaseType.SecondMain, _S10);
        phasesMap.put(MagicPhaseType.EndOfTurn, _S11);
        phasesMap.put(MagicPhaseType.Cleanup, _S12);
    }

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
        for (MagicPhaseType phase : phasesMap.keySet()) {
            setPhaseStepIndicator(phase.getAbbreviation(), MText.get(phasesMap.get(phase)));
        }
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
