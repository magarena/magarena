package magic.ui.screen;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import magic.MagicMain;
import magic.ui.MagicFrame;
import magic.ui.interfaces.IMagActionBar;
import magic.ui.interfaces.IMagScreenOptionsMenu;
import magic.ui.interfaces.IMagStatusBar;
import magic.ui.widget.MagStatusBar;
import magic.ui.widget.MagActionBar;
import net.miginfocom.swing.MigLayout;

/**
 * Base class for a screen ensuring consistent style and layout.
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractScreen extends JPanel {

    private JPanel content;
    private final MagicFrame frame;

    // CTR
    public AbstractScreen() {
        this.frame = (MagicFrame)MagicMain.rootFrame;
        setOpaque(false);
        setEscapeKeyInputMap();
    }

    protected void setContent(final JPanel content) {
        this.content = content;
        doMagScreenLayout();
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
    public abstract boolean isScreenReadyToClose(final AbstractScreen nextScreen);

    protected MagicFrame getFrame() {
        return frame;
    }
}
