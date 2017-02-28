package magic.ui.screen;

import magic.ui.ScreenController;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import magic.ui.WikiPage;
import magic.ui.helpers.KeyEventAction;
import magic.ui.helpers.UrlHelper;
import magic.ui.screen.duel.game.DuelGameScreen;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class MScreen {

    private static final JPanel TEMP_PANEL = new JPanel() {
        @Override
        public boolean isVisible() {
            return false;
        }
    };

    /**
     * Reference to swing component representing a "screen".
     */
    private final JPanel screen = new JPanel();

    private JComponent contentPanel = TEMP_PANEL;
    private WikiPage wikiPage = WikiPage.HOME;
    private ScreenLoaderWorker loadingWorker;

    public MScreen() {
        screen.setOpaque(false);
        screen.setLayout(new MigLayout("insets 0, gap 0", "[fill, grow]", "[fill, grow]"));
        setDefaultKeyboardActions();
    }

    private void doEscapeKeyAction() {
        if (this instanceof DuelGameScreen) {
            ((DuelGameScreen)this).showOptionsMenuOverlay();
        } else {
            ScreenController.closeActiveScreen(true);
        }
    }

    private void showKeywordsScreen() {
        ScreenController.showKeywordsScreen();
    }

    private void setDefaultKeyboardActions() {
        KeyEventAction.doAction(this, this::doEscapeKeyAction)
            .onFocus(0, KeyEvent.VK_ESCAPE);
        KeyEventAction.doAction(this, this::doF1KeyAction)
            .onFocus(0, KeyEvent.VK_F1);
        KeyEventAction.doAction(this, this::showKeywordsScreen)
            .onFocus(0, KeyEvent.VK_K);
    }

    protected void refreshLayout() {
        screen.removeAll();
        screen.add(contentPanel);
        screen.revalidate();
    }

    protected void setMainContent(final JComponent aPanel) {
        this.contentPanel = aPanel;
        refreshLayout();
    }

    private void doF1KeyAction() {
        showWikiHelpPage();
    }

    public void showWikiHelpPage() {
        UrlHelper.openURL(wikiPage.getUrl());
    }

    public boolean isScreenReadyToClose(MScreen aScreen) {
        if (loadingWorker != null && !loadingWorker.isDone()) {
            loadingWorker.cancel(true);
        }
        return true;
    }

    protected JComponent getContentPanel() {
        return contentPanel;
    }

    public void setWikiPage(WikiPage wikiPage) {
        this.wikiPage = wikiPage;
    }

    protected boolean isCardDataRequired() {
        return false;
    }

    /**
     * Displays a loading screen if waiting for card data to be loaded.
     *
     * @param r normally the screen's UI initialization code.
     */
    protected final void useLoadingScreen(Runnable r) {

        final boolean needsCardData =
                isCardDataRequired() && !MagicSystem.loadCards.isDone();

        if (needsCardData) {

            ScreenLoadingPanel loadingPanel = new ScreenLoadingPanel(r, needsCardData);

            loadingWorker = new ScreenLoaderWorker(loadingPanel);
            loadingWorker.execute();

            setMainContent(loadingPanel);

        } else {
            r.run();
        }
    }


    //
    // Swing component delegates.
    //

    protected void setLayout(MigLayout layout) {
        screen.setLayout(layout);
    }

    public void requestFocus() {
        screen.requestFocus();
    }

    public boolean isVisible() {
        return screen.isVisible();
    }

    public void setVisible(boolean isVisible) {
        screen.setVisible(isVisible);
        if (isVisible) {
            refreshLayout();
        }
    }

    protected void removeAll() {
        screen.removeAll();
    }

    protected void add(Component c) {
        screen.add(c);
    }

    protected void revalidate() {
        screen.revalidate();
    }

    public void setCursor(Cursor c) {
        screen.setCursor(c);
    }

    public KeyEventAction getKeyEventAction(Runnable r) {
        return KeyEventAction.doAction(screen, r);
    }

    public void addToLayout(JComponent c, String layout) {
         c.add(screen, layout);
    }

    public void refreshStyle() {
        MScreenHelper.refreshComponentStyle(screen);
    }
    
}
