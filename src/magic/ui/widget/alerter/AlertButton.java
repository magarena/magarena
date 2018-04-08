package magic.ui.widget.alerter;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import magic.ui.MagicSound;

@SuppressWarnings("serial")
public abstract class AlertButton extends JButton {

    private AbstractAction alertAction;

    /**
     * The action to perform when the alert button is clicked.
     */
    protected abstract AbstractAction getAlertAction();

    /**
     * Subclass should implement its alert check code and return the text to
     * show on the alert button if alert is triggered, otherwise empty string.
     * <p>
     * Runs on non-EDT so UI is not affected by lengthy alert check.
     */
    protected abstract String getAlertCaption();

    AlertButton() {
        setFont(getFont().deriveFont(Font.BOLD));
        setVisible(false);
        setMouseListener();
        setAlertAction();
    }

    private void setAlertAction() {
        if (alertAction == null) {
            alertAction = getAlertAction();
            setAction(alertAction);
        }
    }

    private void setMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    /**
     * Alert subclass can override with own sound if required.
     */
    protected void playNewAlertSoundEffect() {
        MagicSound.ALERT.play();
    }

    /**
     * Runs code specific to alert subclass on non-EDT so that UI
     * is not affected since alert check may take a while to complete.
     */
    public void doAlertCheck() {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                return getAlertCaption();
            }
            @Override
            protected void done() {
                try {
                    setText(get().trim());
                    if (!getText().isEmpty()) {
                        playNewAlertSoundEffect();
                        setVisible(true);
                    } else {
                        setVisible(false);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }.execute();
    }

}
