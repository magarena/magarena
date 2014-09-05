package magic.ui.duel.viewer;

import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.PanelButton;
import magic.ui.widget.TextLabel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Set;

public class PermanentButton extends PanelButton implements ChoiceViewer {

    private static final long serialVersionUID = 1L;

    private final PermanentViewerInfo permanentInfo;
    private final GameController controller;

    public PermanentButton(
            final PermanentViewerInfo permanentInfo,
            final GameController controller,
            final Border border,
            final int maxWidth) {

        this.permanentInfo=permanentInfo;
        this.controller=controller;

        final Theme theme=ThemeFactory.getInstance().getCurrentTheme();

        final JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout(0,2));
        panel.setBorder(border);
        panel.setOpaque(false);

        final JPanel topPanel=new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BorderLayout());
        panel.add(topPanel,BorderLayout.NORTH);

        final JLabel nameLabel=new JLabel(permanentInfo.name);
        nameLabel.setForeground(theme.getNameColor());
        nameLabel.setIcon(permanentInfo.icon);
        topPanel.add(nameLabel,BorderLayout.CENTER);

        final JLabel ptLabel=new JLabel("");
        ptLabel.setForeground(theme.getTextColor());
        if (!permanentInfo.powerToughness.isEmpty()) {
            ptLabel.setText(permanentInfo.powerToughness);
            topPanel.add(ptLabel,BorderLayout.EAST);
        }

        final TextLabel textLabel=new TextLabel(permanentInfo.text,maxWidth-6,false);
        panel.add(textLabel,BorderLayout.CENTER);

        setComponent(panel);
        showValidChoices(controller.getValidChoices());
    }

    @Override
    public void mouseClicked() {
        controller.processClick(permanentInfo.permanent);
    }

    @Override
    public void mouseEntered() {}

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        setValid(validChoices.contains(permanentInfo.permanent));
    }

    @Override
    public Color getValidColor() {
        if (controller.isCombatChoice()) {
            return ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_COMBAT_CHOICE);
        } else {
            return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
        }
    }
}
