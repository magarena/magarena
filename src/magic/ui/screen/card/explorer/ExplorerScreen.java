package magic.ui.screen.card.explorer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.data.MagicSetDefinitions;
import magic.translate.MText;
import magic.ui.MagicLogs;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.WikiPage;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.MScreen;
import magic.ui.screen.cardflow.ICardFlowListener;
import magic.ui.screen.cardflow.ICardFlowProvider;
import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.widget.cards.table.CardsTableStyle;

@SuppressWarnings("serial")
public class ExplorerScreen extends HeaderFooterScreen
    implements ICardFlowProvider, ICardFlowListener {

    // translatable strings
    private static final String _S1 = "Card Explorer";
    private static final String _S3 = "View Script";
    private static final String _S4 = "View the script and groovy files for the selected card.<br>(or double-click row)";
    private static final String _S5 = "Lucky Dip";
    private static final String _S6 = "Selects a random card from the list of cards displayed.";
    private static final String _S7 = "Card flow screen";
    private static final String _S8 = "Browse through the card images starting at the selected card.";

    private static final ImageIcon CARDFLOW_ICON = ImageHelper.getRecoloredIcon(
        MagicIcon.CARDFLOW, Color.BLACK, Color.WHITE
    );

    private ExplorerContentPanel contentPanel;
    private ExplorerHeaderPanel headerPanel;

    public ExplorerScreen() {
        super(MText.get(_S1));
        useCardsLoadingScreen(this::initUI);
    }

    @Override
    protected boolean needsPlayableCards() {
        return true;
    }

    @Override
    protected boolean needsMissingCards() {
        return true;
    }

    private void initUI() {
        headerPanel = new ExplorerHeaderPanel();
        contentPanel = new ExplorerContentPanel(this);
        setHeaderContent(headerPanel);
        setHeaderOptions(new OptionsPanel(this));
        setMainContent(contentPanel);
        setFooterButtons();
        setWikiPage(WikiPage.CARDS_EXPLORER);
    }

    private void setFooterButtons() {
        addFooterGroup(
            PlainMenuButton.build(this::doShowCardFlowScreen,
                CARDFLOW_ICON, MText.get(_S7), MText.get(_S8)
            ),
            PlainMenuButton.build(this::doShowScriptScreen,
                MagicIcon.EDIT, MText.get(_S3), MText.get(_S4)
            )
        );
        addToFooter(
            PlainMenuButton.build(this::doSelectRandomCard,
                MagicIcon.RANDOM, MText.get(_S5), MText.get(_S6)
            )
        );
    }

    private void doShowCardFlowScreen() {
        if (contentPanel.getCardsCount() > 0) {
            ScreenController.showCardFlowScreen(this, this, MText.get(_S1));
        } else {
            MagicSound.BEEP.play();
        }
    }

    private void doShowScriptScreen() {
        contentPanel.showCardScriptScreen();
    }

    private void doSelectRandomCard() {
        contentPanel.selectRandomCard();
    }

    public void doSwitchLayout() {
        ExplorerScreenLayout.setNextLayout();
        contentPanel.refreshLayout();
    }

    @Override
    public boolean isScreenReadyToClose(MScreen nextScreen) {
        if (super.isScreenReadyToClose(nextScreen)) {
            MagicSetDefinitions.clearLoadedSets();
            MagicLogs.clearLoadedLogs();
            return true;
        }
        return false;
    }

    public void refreshTotals(int total, int playable, int missing) {
        headerPanel.refreshTotals(total, playable, missing);
    }

    void setCardsTableStyle(int dialPosition) {
        CardsTableStyle.setStyle(dialPosition);
        contentPanel.setCardsTableStyle();
    }

    @Override
    public BufferedImage getImage(int index) {
        return contentPanel.getCardImage(index);
    }

    @Override
    public int getImagesCount() {
        return contentPanel.getCardsCount();
    }

    @Override
    public int getStartImageIndex() {
        return contentPanel.getSelectedCardIndex();
    }

    @Override
    public void setNewActiveImage(int index) {
        SwingUtilities.invokeLater(() -> contentPanel.setCardAt(index));
    }

    @Override
    public void cardFlowClicked() {
        // not supported
    }
}
