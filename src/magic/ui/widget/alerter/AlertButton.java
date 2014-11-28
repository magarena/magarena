package magic.ui.widget.alerter;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import magic.data.SoundEffects;

@SuppressWarnings("serial")
public abstract class AlertButton extends JButton {

    protected abstract AbstractAction getAlertAction();

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

    protected void playNewAlertSoundEffect() {
        SoundEffects.playClip(SoundEffects.RESOLVE_SOUND);
    }

}
