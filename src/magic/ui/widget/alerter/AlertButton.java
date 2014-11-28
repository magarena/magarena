package magic.ui.widget.alerter;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import magic.data.SoundEffects;

@SuppressWarnings("serial")
public abstract class AlertButton extends JButton {

    protected abstract AbstractAction getAlertAction();
    protected abstract String getAlertCaption();

    AlertButton() {
        setFont(getFont().deriveFont(Font.BOLD));
        setVisible(false);
        setMouseListener();
        setAction(getAlertAction());
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
        SoundEffects.playClip(SoundEffects.RESOLVE_SOUND);
    }

    public void doAlertCheck() {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
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
