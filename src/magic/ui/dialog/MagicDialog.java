package magic.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import magic.ui.utility.MagicStyle;
import magic.ui.theme.Theme;
import magic.ui.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class MagicDialog extends JDialog {

    protected abstract AbstractAction getCancelAction();

    private final MigLayout migLayout = new MigLayout();
    private final JPanel contentPanel = new JPanel();

    public MagicDialog(Frame owner, String title, Dimension size) {
        super(owner);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(size);
        setTitle(title);
        setEscapeKeyAction();

        migLayout.setLayoutConstraints("flowy, insets 0");
        getContentComponent().setLayout(migLayout);

        setLookAndFeel();
        refreshLayout();

    }

    protected JPanel getDialogContentPanel() {
        return contentPanel;
    }

    private void refreshLayout() {
        final JComponent content = getContentComponent();
        content.removeAll();
        content.add(getDialogCaptionLabel(), "w 100%, h 26!");
        content.add(contentPanel, "w 100%, h 100%");
    }

    private void setLookAndFeel() {
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentComponent().setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY),
                        BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)))
        );
    }

    private JLabel getDialogCaptionLabel() {
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

    private JComponent getContentComponent() {
        return (JComponent)getContentPane();
    }

}
