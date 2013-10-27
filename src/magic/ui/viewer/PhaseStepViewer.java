package magic.ui.viewer;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import magic.model.phase.MagicPhaseType;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class PhaseStepViewer extends TexturedPanel {

    private int currentPhaseStep = -1;
    private final Theme theme = ThemeFactory.getInstance().getCurrentTheme();
    private final Dimension gap = new Dimension(8, 0);

    // CTR
    public PhaseStepViewer() {
        setMyLayout();
    }

    private void setMyLayout() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createRigidArea(new Dimension(9, 0)));
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
                lbl.setForeground(theme.getTextColor());
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
        add(lbl);
        add(Box.createRigidArea(gap));
    }

    public void setPhaseStep(MagicPhaseType gamePhaseType) {
        switch (gamePhaseType) {
        case Mulligan:          { setPhaseStep(-1); break; }
        case Untap:             { setPhaseStep(1); break; }
        case Upkeep:            { setPhaseStep(3); break; }
        case Draw:              { setPhaseStep(5); break; }
        case FirstMain:         { setPhaseStep(7); break; }
        case BeginOfCombat:     { setPhaseStep(9); break; }
        case DeclareAttackers:  { setPhaseStep(11); break; }
        case DeclareBlockers:   { setPhaseStep(13); break; }
        case CombatDamage:      { setPhaseStep(15); break; }
        case EndOfCombat:       { setPhaseStep(17); break; }
        case SecondMain:        { setPhaseStep(19); break; }
        case EndOfTurn:         { setPhaseStep(21); break; }
        case Cleanup:           { setPhaseStep(23); break; }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, 22);
    }

}
