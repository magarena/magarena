package magic.ui.screen;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import magic.ui.URLUtils;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBar;
import magic.ui.screen.widget.StatusBar;
import net.miginfocom.swing.MigLayout;

/**
 * Base class for a screen ensuring consistent style and layout.
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractScreen extends JPanel {

    private JComponent content;
    private final MagicFrame frame;
    private ActionBar actionbar;

    // CTR
    public AbstractScreen() {
        this.frame = ScreenController.getMainFrame();
        setOpaque(false);
        setEscapeKeyInputMap();
        setF1KeyInputMap();
    }

    protected void refreshActionBar() {
        actionbar.refreshLayout();
    }

    protected void setContent(final JComponent content) {
        this.content = content;
        doMigLayout();
        revalidate();
        repaint();
    }

    private void doMigLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        layoutMagStatusBar();
        add(this.content, "w 100%, h 100%");
        layoutMagActionBar();
    }

    private void layoutMagStatusBar() {
        if (hasStatusBar()) {
            add(new StatusBar(this), "w 100%, h 50!");
        }
    }

    private void layoutMagActionBar() {
        if (hasActionBar()) {
            this.actionbar = new ActionBar((IActionBar)this);
            add(actionbar, "w 100%, h 50!");
        }
    }

    private void setEscapeKeyInputMap() {
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "OptionsMenu");
        getActionMap().put("OptionsMenu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showOptionsMenuOrCloseScreen();
            }
        });
    }

    private void setF1KeyInputMap() {
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "WikiHelp");
        getActionMap().put("WikiHelp", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showWikiHelpPage();
            }
        });
    }

    public void showWikiHelpPage() {
        if (this.hasWikiPage()) {
            URLUtils.openURL(URLUtils.URL_WIKI + ((IWikiPage)this).getWikiPageName());
        }
    }

    private void showOptionsMenuOrCloseScreen() {
        if (this.hasOptionsMenu()) {
            ((IOptionsMenu)this).showOptionsMenuOverlay();
        } else {
            ScreenController.closeActiveScreen(true);
        }
    }

    public boolean hasOptionsMenu() {
        return this instanceof IOptionsMenu;
    };

    private boolean hasActionBar() {
        return this instanceof IActionBar;
    }

    private boolean hasStatusBar() {
        return this instanceof IStatusBar;
    }

    public boolean hasWikiPage() {
        return this instanceof IWikiPage;
    }

    /**
     * Gives the active screen the chance to prevent closing.
     */
    public abstract boolean isScreenReadyToClose(final AbstractScreen nextScreen);

    protected MagicFrame getFrame() {
        return frame;
    }

}
