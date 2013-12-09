package magic.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import magic.ui.widget.MagStatusBar;
import magic.ui.widget.MagActionBar;
import net.miginfocom.swing.MigLayout;

/**
 * Base class for a screen ensuring consistent style and layout.
 *
 */
@SuppressWarnings("serial")
public abstract class MagScreen extends JPanel {

    private final JPanel content;
    private final MagicFrame frame;

    public MagScreen(final JPanel content0, final MagicFrame frame0) {
        this.content = content0;
        this.frame = frame0;
        setOpaque(false);
        doMagScreenLayout();
        setEscapeKeyInputMap();
    }

    private void doMagScreenLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        layoutMagStatusBar();
        add(this.content, "w 100%, h 100%");
        layoutMagActionBar();
    }

    private void layoutMagStatusBar() {
        if (hasStatusBar()) {
            add(new MagStatusBar(this, frame), "w 100%");
        }
    }

    private void layoutMagActionBar() {
        if (hasActionBar()) {
            add(new MagActionBar((IMagActionBar)this), "w 100%");
        }
    }

    private void setEscapeKeyInputMap() {
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "OptionsMenu");
        getActionMap().put("OptionsMenu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showOptionsMenuOrCloseScreen();
            }
        });
    }

    private void showOptionsMenuOrCloseScreen() {
        if (this.hasOptionsMenu()) {
            ((IMagScreenOptionsMenu)this).showOptionsMenuOverlay();
        } else {
            frame.closeActiveScreen(true);
        }
    }

    public boolean hasOptionsMenu() {
       return this instanceof IMagScreenOptionsMenu;
    };

    private boolean hasActionBar() {
        return this instanceof IMagActionBar;
    }

    private boolean hasStatusBar() {
        return this instanceof IMagStatusBar;
    }

    /**
     * Gives the active screen the chance to prevent closing.
     */
    public abstract boolean isScreenReadyToClose(final MagScreen nextScreen);

}
