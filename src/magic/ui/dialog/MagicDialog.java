package magic.ui.dialog;

import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import magic.ui.MagicStyle;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
public abstract class MagicDialog extends JDialog {

    protected abstract AbstractAction getCancelAction();

    public MagicDialog(Frame owner, String title, Dimension size) {
        super(owner, true);
        setSize(size);
        setTitle(title);
        setEscapeKeyAction();
    }

    protected void setLookAndFeel() {
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
    }
    
    protected JLabel getDialogCaptionLabel() {
        final JLabel lbl = new JLabel(getTitle());
        lbl.setOpaque(true);
        lbl.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
        lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void setEscapeKeyAction() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");
        root.getActionMap().put("closeDialog", getCancelAction());
    }
    
}
