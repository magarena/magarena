package magic.ui.screen.widget;

import magic.ui.MagicImages;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import magic.ui.ScreenController;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class CaptionPanel extends JPanel {

    // translatable strings
    @StringContext(eg = "show Magarena About dialog.")
    private static final String _S1 = "About...";

    private final static ImageIcon wizardIcon = new ImageIcon(MagicImages.MENU_LOGO);

    private final String screenCaption;

    public CaptionPanel(final String screenCaption0) {
        this.screenCaption = screenCaption0;
        setLayout(new MigLayout("insets 0, gap 4, flowx"));
        setOpaque(false);
        add(getWizardIconButton());
        add(getTitlePanel());
    }

    private JButton getWizardIconButton() {
        JButton btn = new JButton(wizardIcon);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(UiString.get(_S1));
        setButtonTransparent(btn);
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenController.showAboutDialog();
            }
        });
        return btn;
    }

    private void setButtonTransparent(final JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(null);
    }

    private JPanel getTitlePanel() {
        JPanel titlePanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
        titlePanel.setOpaque(false);
        JLabel version = new JLabel(MagicSystem.SOFTWARE_TITLE);
        version.setFont(FontsAndBorders.FONT0);
        version.setForeground(Color.WHITE);
        titlePanel.add(version);
        titlePanel.add(getScreenCaptionLabel());
        return titlePanel;
    }

    private JLabel getScreenCaptionLabel() {
        final JLabel lbl = new JLabel(this.screenCaption);
        lbl.setFont(FontsAndBorders.FONT_MENU_BUTTON);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

}
