package magic.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicFrame;
import magic.ui.dialog.button.CancelButton;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.firemind.FiremindWorkerPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class FiremindWorkerDialog extends JDialog implements ActionListener, PropertyChangeListener {

    // translatable strings
    private static final String _S1 = "Run Firemind Worker";
    private static final String _S3 = "Run in background...";

    private final JButton cancelButton = new CancelButton();
    private final JButton backgroundButton = new JButton();
    private final FiremindWorkerPanel firemindWorkerPanel = new FiremindWorkerPanel();

    public FiremindWorkerDialog(final MagicFrame frame) {
        super(frame, true);

        firemindWorkerPanel.addPropertyChangeListener("isRunning", this);
        setLookAndFeel();
        refreshLayout();
        setEscapeKeyAsCancelAction();
        this.setLocationRelativeTo(frame);
        this.setVisible(true);
    }

    private JPanel getFieldsPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy, insets 8, gapy 10"));
        panel.add(firemindWorkerPanel, "w 100%");
        return panel;
    }

    private void setEscapeKeyAsCancelAction() {
        final KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!backgroundButton.isEnabled()) {
                    doCancelAndClose();
                }
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void refreshLayout() {
        setLayout(new MigLayout("flowy, gapy 0, insets 0"));
        add(getDialogCaptionLabel(), "w 100%, h 26!");
        add(getFieldsPanel(), "w 100%");
        add(getButtonPanel(), "w 100%, aligny bottom, pushy");
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

    private void setLookAndFeel() {
        setTitle(MText.get(_S1));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(360, 460);
        setUndecorated(true);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));
        backgroundButton.setEnabled(false);
    }


    private void updateComponentState() {
        boolean isBackgroundButtonEnabled = false;
        final boolean isRunning = firemindWorkerPanel.isRunning();
        isBackgroundButtonEnabled = isBackgroundButtonEnabled || isRunning;
        backgroundButton.setEnabled(isBackgroundButtonEnabled);
    }

    private JPanel getButtonPanel() {
        // cancel button
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);
        // background button
        backgroundButton.setText(MText.get(_S3));
        backgroundButton.setFocusable(false);
        backgroundButton.addActionListener(this);
        // layout
        final JPanel panel = new JPanel(new MigLayout());
        panel.add(backgroundButton, "w 100%, alignx left");
        panel.add(cancelButton, "w 100!, alignx right, pushx");
        return panel;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==cancelButton) {
            doCancelAndClose();
        } else if (source == backgroundButton) {
            setVisible(false);
        }
    }

    private void doCancelAndClose() {
        firemindWorkerPanel.doCancel();
        dispose();
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("isRunning")) {
            updateComponentState();
        }
    }

}
