package magic.ui.image.download;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DownloadDialogPanel extends TexturedPanel implements PropertyChangeListener {

    private static final String _S1 = "Download card images";

    private final DialogMainPanel mainPanel;

    public DownloadDialogPanel() {

        setPreferredSize(new Dimension(460, 520));
        setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)))
        );

        mainPanel = getMainPanel();

        setLayout(new MigLayout("flowy, insets 0"));
        add(getDialogCaptionLabel(), "w 100%, h 26!");
        add(mainPanel, "w 100%, h 100%");

        doSetStyle(this);
    }

    private DialogMainPanel getMainPanel() {
        final DialogMainPanel panel = new DialogMainPanel();
        panel.addPropertyChangeListener(DialogMainPanel.CP_CLOSE, this);
        panel.addPropertyChangeListener(DialogMainPanel.CP_RUN_BACKGROUND, this);
        return panel;
    }

    private JLabel getDialogCaptionLabel() {
        final JLabel lbl = new JLabel(UiString.get(_S1));
        lbl.setOpaque(true);
        lbl.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
        lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void doCloseScreen() {
        MagicImages.clearCache();
        ScreenController.closeActiveScreen(false);
    }

    private void doHideScreen() {
        MagicImages.clearCache();
        ScreenController.hideActiveScreen();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case DialogMainPanel.CP_CLOSE:
                doCloseScreen();
                break;
            case DialogMainPanel.CP_RUN_BACKGROUND:
                doHideScreen();
                break;
        }
    }

    public boolean isBusy() {
        return mainPanel.isBusy();
    }

    private void doSetStyle(JPanel panel) {
        synchronized (panel.getTreeLock()) {
            for (Component c : panel.getComponents()) {
                if (c instanceof JPanel) {
                    doSetStyle((JPanel) c);
                }
                c.setFocusable(false);
            }
            if (this != panel) {
                panel.setOpaque(false);
            }
        }
    }

}
