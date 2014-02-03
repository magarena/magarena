package magic.ui.screen;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import magic.MagicMain;
import magic.data.IconImages;
import magic.ui.MagicFrame;
import magic.ui.screen.interfaces.IMagActionBar;
import magic.ui.screen.interfaces.IMagScreenOptionsMenu;
import magic.ui.screen.interfaces.IMagStatusBar;
import magic.ui.screen.widget.ActionBar;
import magic.ui.screen.widget.StatusBar;
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
        setBusy(true);
        setOpaque(false);
        setEscapeKeyInputMap();
    }

    protected void setContent(final JPanel content) {
        this.content = content;
        doMagScreenLayout();
        setBusy(false);
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
            add(new StatusBar(this, frame), "w 100%");
        }
    }

    private void layoutMagActionBar() {
        if (hasActionBar()) {
            add(new ActionBar((IMagActionBar)this), "w 100%");
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

    public void setBusy(final boolean isBusy) {
        if (isBusy) {
          final ImageIcon ii = IconImages.BUSY;
          final JPanel pnl = new JPanel(new MigLayout("insets 0, gap 0"));
          pnl.setOpaque(false);
          final JLabel lbl = new JLabel(ii);
          lbl.setHorizontalAlignment(SwingConstants.CENTER);
          lbl.setOpaque(false);
          pnl.add(lbl, "w 100%, h 100%");
          frame.setGlassPane(pnl);
          pnl.setVisible(true);
      } else {
          frame.getGlassPane().setVisible(false);
      }

    }

}
