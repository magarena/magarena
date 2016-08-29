package magic.ui.explorer.filter;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.ui.ScreenController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
class ButtonControlledPopup extends TexturedPanel implements ActionListener, WindowFocusListener {

    public static final int STARTING_WIDTH = 260;
    public static final int STARTING_HEIGHT = 300;

    private final JDialog dialog;
    private final JButton invokePopupButton;
    private final String hidePopupButtonText;
    private final String showPopupButtonText;
    private final Timer timer;

    private boolean popupJustToggled;

    ButtonControlledPopup(JButton toggleButton, final String hidePopupButtonText, final String showPopupButtonText) {
        this.invokePopupButton = toggleButton;
        this.hidePopupButtonText = hidePopupButtonText;
        this.showPopupButtonText = showPopupButtonText;
        this.dialog = new JDialog(ScreenController.getMainFrame());
        this.timer = new Timer();

        setBorder(FontsAndBorders.UP_BORDER);

        dialog.setUndecorated(true);
        dialog.setSize(STARTING_WIDTH, STARTING_HEIGHT);

        invokePopupButton.addActionListener(this);
        dialog.addWindowFocusListener(this);
        dialog.add(this);

        setEscapeKeyAction();
    }

    private void setEscapeKeyAction() {
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");

        @SuppressWarnings("serial")
        final AbstractAction closeAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                hidePopup();
            }
        };
        root.getActionMap().put("closeDialog", closeAction);
    }

    public void setPopupSize(final int width, final int height) {
        // jdialog will not resize by itself
        dialog.setSize(width, height);
    }

    public void showPopup() {
        // set location relative to button
        final Point location = invokePopupButton.getLocation();
        SwingUtilities.convertPointToScreen(location, invokePopupButton.getParent());
        location.translate(0, invokePopupButton.getHeight());
        dialog.setLocation(location);

        // showPopup the popup if not visible
        invokePopupButton.setText(hidePopupButtonText);
        dialog.setVisible(true);
    }

    public void hidePopup() {
        invokePopupButton.setText(showPopupButtonText);
        dialog.setVisible(false);
    }

    class ResponsePreventer extends TimerTask {
        @Override
        public void run() {
            popupJustToggled = false;
        }
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (popupJustToggled) {
            // focus event just hid popup -> this event is probably from clicking on the hide button -> don't do anything
            return;
        }

        // set popup visibility
        if (!dialog.isVisible()) {
            showPopup();
        } else {
            // hide - taken care of by focusLost
        }
    }

    @Override
    public void windowLostFocus(final WindowEvent e) {
        popupJustToggled = true;
        timer.schedule(new ResponsePreventer(), 300); // don't allow clicks on hide button to reshow popup immediately by disabling response for < 1 s

        if (dialog.isVisible()) {
            hidePopup();
        }
    }

    @Override
    public void windowGainedFocus(final WindowEvent e) {
        setFocusToFirstFilterOption(this);
    }

    /**
     * Sets focus to the first filter option.
     * <p>
     * This is a generic routine that ensures that container components such
     * as JPanel or JScrollPane which contain the filter options do not receive
     * the initial focus.
     * <p>
     * You can control the focus using a component's setFocusable() method.
     * Note that if this is a container then it will apply to all its children as well.
     */
    private boolean setFocusToFirstFilterOption(final JComponent container) {
        boolean isFocusSet = false;
        for (Component component : container.getComponents()) {
            if (component.isFocusable() && component instanceof JComponent) {
                final JComponent jc = (JComponent)component;
                if (jc.getComponents().length > 0) {
                    if (setFocusToFirstFilterOption(jc)) {
                        isFocusSet = true;
                        break;
                    }
                }
                jc.requestFocusInWindow();
                isFocusSet = true;
                break;
            }
        }
        return isFocusSet;
    }
}
