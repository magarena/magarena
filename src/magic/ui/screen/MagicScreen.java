package magic.ui.screen;

import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import magic.cardBuilder.renderers.CardBuilder;
import magic.ui.WikiPage;
import magic.ui.helpers.UrlHelper;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class MagicScreen extends JPanel {

    private static final JPanel TEMP_PANEL = new JPanel() {
        @Override
        public boolean isVisible() {
            return false;
        }
    };

    private JComponent contentPanel = TEMP_PANEL;
    private WikiPage wikiPage = WikiPage.HOME;
    private ScreenLoaderWorker loadingWorker;

    public MagicScreen() {
        setOpaque(false);
        setDefaultKeyboardActions();
        setLayout(new MigLayout("insets 0, gap 0", "[fill, grow]", "[fill, grow]"));
    }

    private void setDefaultKeyboardActions() {
        ScreenHelper.setKeyEvent(this, KeyEvent.VK_F1, this::doF1KeyAction);
    }

    protected void refreshLayout() {
        removeAll();
        add(contentPanel);
        revalidate();
    }

    protected void setMainContent(final JComponent aPanel) {
        this.contentPanel = aPanel;
        refreshLayout();
    }

    protected void doF1KeyAction() {
        showWikiHelpPage();
    }

    public void showWikiHelpPage() {
        UrlHelper.openURL(wikiPage.getUrl());
    }

    public boolean isScreenReadyToClose(final Object aScreen) {
        if (loadingWorker != null && !loadingWorker.isDone()) {
            loadingWorker.cancel(true);
        }
        return true;
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (isVisible) {
            refreshLayout();
        }
    }

    protected JComponent getContentPanel() {
        return contentPanel;
    }

    public void setWikiPage(WikiPage wikiPage) {
        this.wikiPage = wikiPage;
    }

    protected boolean isCardBuilderRequired() {
        return false;
    }

    protected boolean isCardDataRequired() {
        return false;
    }

    /**
     * Displays a loading screen if waiting for
     * CardBuilder or card data to be loaded.
     *
     * @param r normally the screen's UI initialization code.
     */
    protected final void useLoadingScreen(Runnable r) {

        final boolean needsCBuilder =
                isCardBuilderRequired() && !CardBuilder.IS_LOADED;

        final boolean needsCardData =
                isCardDataRequired() && !MagicSystem.loadCards.isDone();

        if (needsCardData || needsCBuilder) {

            ScreenLoadingPanel loadingPanel = new ScreenLoadingPanel(
                    r, needsCBuilder, needsCardData
            );

            loadingWorker = new ScreenLoaderWorker(loadingPanel);
            loadingWorker.execute();

            setMainContent(loadingPanel);

        } else {
            r.run();
        }
    }

}
