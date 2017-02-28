package magic.ui;

import java.awt.GraphicsEnvironment;
import java.util.Stack;
import java.util.concurrent.Callable;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.IUIGameController;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicDeck;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.player.IPlayerProfileListener;
import magic.model.player.PlayerProfile;
import magic.translate.MText;
import magic.ui.dialog.DuelSidebarLayoutDialog;
import magic.ui.dialog.prefs.PreferencesDialog;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.MScreen;
import magic.ui.screen.about.AboutScreen;
import magic.ui.screen.card.explorer.ExplorerScreen;
import magic.ui.screen.card.script.CardScriptScreen;
import magic.ui.screen.deck.DeckScreen;
import magic.ui.screen.deck.editor.DeckEditorScreen;
import magic.ui.screen.deck.editor.DeckEditorSplitScreen;
import magic.ui.screen.deck.editor.IDeckEditorClient;
import magic.ui.screen.deck.hand.SampleHandScreen;
import magic.ui.screen.deck.tiled.DeckTiledCardsScreen;
import magic.ui.screen.decks.DecksScreen;
import magic.ui.screen.duel.decks.DuelDecksScreen;
import magic.ui.screen.duel.game.DuelGameScreen;
import magic.ui.screen.duel.game.log.GameLogScreen;
import magic.ui.screen.duel.mulligan.MulliganScreen;
import magic.ui.screen.duel.player.SelectAiPlayerScreen;
import magic.ui.screen.duel.player.SelectHumanPlayerScreen;
import magic.ui.screen.duel.player.avatar.AvatarImagesScreen;
import magic.ui.screen.duel.player.zone.CardZoneScreen;
import magic.ui.screen.duel.setup.NewDuelSettingsScreen;
import magic.ui.screen.images.download.DownloadImagesScreen;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.keywords.KeywordsScreen;
import magic.ui.screen.menu.dev.DevMenuScreen;
import magic.ui.screen.menu.help.HelpMenuScreen;
import magic.ui.screen.menu.language.StartScreen;
import magic.ui.screen.menu.main.MainMenuScreen;
import magic.ui.screen.menu.migrate.ImportScreen;
import magic.ui.screen.menu.settings.SettingsMenuScreen;
import magic.ui.screen.player.PlayerScreen;
import magic.ui.screen.readme.ReadmeScreen;
import magic.ui.screen.stats.StatsScreen;
import magic.ui.screen.test.TestScreen;
import magic.ui.widget.duel.choice.MulliganChoicePanel;
import magic.utility.MagicSystem;

public final class ScreenController {

    // translatable strings.
    private static final String _S2 = "Information";
    private static final String _S3 = "Warning";

    private static MagicFrame mainFrame;
    static {
        if (!GraphicsEnvironment.isHeadless()) {
            mainFrame = new MagicFrame(MagicSystem.SOFTWARE_TITLE);
        }
    }

    private static final Stack<MScreen> screens = new Stack<>();
    private static MScreen hiddenScreen;

    public static MagicFrame getFrame() {
        return mainFrame;
    }

    public static void showPreferencesDialog() {
        final PreferencesDialog dialog = new PreferencesDialog(mainFrame, isDuelActive());
        if (dialog.isRestartRequired()) {
            SwingUtilities.invokeLater(ScreenController::showMainMenuScreen);
        }
    }

    private static void doCloseActiveScreen() {
        final MScreen activeScreen = screens.pop();
        final MScreen nextScreen = screens.peek();
        if (activeScreen.isScreenReadyToClose(nextScreen)) {
            showScreen(screens.pop());
            if (nextScreen instanceof DuelGameScreen) {
                ((DuelGameScreen) nextScreen).updateView();
            } else if (nextScreen instanceof MainMenuScreen) {
                ((MainMenuScreen) nextScreen).updateMissingImagesNotification();
            }
        } else {
            screens.push(activeScreen);
        }
    }

    public static void closeActiveScreen(final boolean isEscapeKeyAction) {
        if (screens.size() == 1) {
            mainFrame.quitToDesktop(isEscapeKeyAction);
        } else {
            doCloseActiveScreen();
        }
    }

    public static void closeActiveScreen() {
        closeActiveScreen(false);
    }

    public static MScreen getActiveScreen() {
        return screens.peek();
    }

    public static void refreshStyle() {
        screens.forEach(MScreen::refreshStyle);
    }

    public static void showInfoMessage(final String message) {
        JOptionPane.showMessageDialog(mainFrame, message, MText.get(_S2), JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningMessage(final String message) {
        MagicSound.BEEP.play();
        JOptionPane.showMessageDialog(mainFrame, message, MText.get(_S3), JOptionPane.WARNING_MESSAGE);
    }

    public static void showDuelSidebarDialog(final IUIGameController controller) {
        new DuelSidebarLayoutDialog(controller);
    }

    public static boolean isDuelActive() {
        for (MScreen screen : screens) {
            if (screen instanceof DuelDecksScreen || screen instanceof DuelGameScreen) {
                return true;
            }
        }
        return false;
    }

    public static void hideActiveScreen() {
        if (hiddenScreen == null) {
            hiddenScreen = screens.pop();
            hiddenScreen.setVisible(false);
            showScreen(screens.pop());
        } else {
            throw new RuntimeException("A screen is already hidden - only one allowed at a time!");
        }
    }

    private static boolean isDuelDecksScreenDisplayed() {
        return !screens.isEmpty() && screens.peek() instanceof DuelDecksScreen;
    }

    private static void setMainFrameScreen(final MScreen screen) {
        mainFrame.setScreen(screen);
    }

    private static boolean isScreenLoadedButHidden(MScreen screen) {
        return hiddenScreen != null && hiddenScreen.getClass().getName().equals(screen.getClass().getName());
    }

    private static void showScreen(MScreen screen) {
        if (isScreenLoadedButHidden(screen)) {
            screen = hiddenScreen;
            hiddenScreen = null;
        }
        setMainFrameScreen(screen);
        screens.push(screen);
        screen.setVisible(true);
        screen.requestFocus();
    }

    private static void showScreen(Callable<MScreen> c) {
        MouseHelper.showBusyCursor();
        try {
            showScreen(c.call());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            MouseHelper.showDefaultCursor();
        }
    }

    //
    // public show screens methods
    //

    public static void showDevMenuScreen() {
        showScreen(DevMenuScreen::new);
    }

    public static void showTestScreen() {
        showScreen(TestScreen::new);
    }

    public static void showStatsScreen() {
        showScreen(StatsScreen::new);
    }

    public static void showDuelDecksScreen(final MagicDuel duel) {
        if (isDuelDecksScreenDisplayed()) {
            screens.pop();
        }
        showScreen(() -> new DuelDecksScreen(duel));
    }

    public static void showMainMenuScreen() {
        screens.clear();
        showScreen(MainMenuScreen::new);
    }

    public static void showReadMeScreen() {
        showScreen(ReadmeScreen::new);
    }

    public static void showKeywordsScreen() {
        showScreen(KeywordsScreen::new);
    }

    public static void showHelpMenuScreen() {
        showScreen(HelpMenuScreen::new);
    }

    public static void showSettingsMenuScreen() {
        showScreen(SettingsMenuScreen::new);
    }

    public static void showCardExplorerScreen() {
        showScreen(ExplorerScreen::new);
    }

    public static void showDeckEditor(MagicDeck aDeck) {
        showScreen(GeneralConfig.getInstance().isSplitViewDeckEditor()
                ? () -> new DeckEditorSplitScreen(aDeck)
                : () -> new DeckEditorScreen(aDeck));
    }

    public static void showDeckEditor(IDeckEditorClient supplier) {
        showScreen(GeneralConfig.getInstance().isSplitViewDeckEditor()
                ? () -> new DeckEditorSplitScreen(supplier.getDeck())
                : () -> new DeckEditorScreen(supplier));
    }

    public static void showDeckEditor() {
        showScreen(GeneralConfig.getInstance().isSplitViewDeckEditor()
                ? DeckEditorSplitScreen::new
                : DeckEditorScreen::new);
    }

    public static void showDeckScreen(MagicDeck deck, MagicCardDefinition selectedCard) {
        showScreen(() -> new DeckScreen(deck, selectedCard));
    }

    public static void showDeckScreen(MagicDeck deck, String title) {
        showScreen(() -> new DeckScreen(deck, title));
    }

    public static void showSampleHandScreen(final MagicDeck deck) {
        showScreen(() -> new SampleHandScreen(deck));
    }

    public static void showCardZoneScreen(final MagicCardList cards, final String zoneName, final boolean animateCards) {
        showScreen(() -> new CardZoneScreen(cards, zoneName, animateCards));
    }

    public static void showMulliganScreen(final MulliganChoicePanel choicePanel, final MagicCardList hand) {
        if (screens.peek() instanceof MulliganScreen) {
            MulliganScreen screen = (MulliganScreen) screens.peek();
            screen.dealNewHand(choicePanel, hand);
        } else {
            showScreen(() -> new MulliganScreen(choicePanel, hand));
        }
    }

    public static void showDeckTiledCardsScreen(final MagicDeck deck) {
        showScreen(() -> new DeckTiledCardsScreen(deck));
    }

    public static void showSelectAiProfileScreen(final IPlayerProfileListener listener, final PlayerProfile profile) {
        showScreen(() -> new SelectAiPlayerScreen(listener, profile));
    }

    public static void showSelectHumanPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile profile) {
        showScreen(() -> new SelectHumanPlayerScreen(listener, profile));
    }

    public static void showAvatarImagesScreen(final IAvatarImageConsumer consumer) {
        showScreen(() -> new AvatarImagesScreen(consumer));
    }

    public static void showNewDuelSettingsScreen() {
        showScreen(NewDuelSettingsScreen::new);
    }

    public static void showGameLogScreen() {
        showScreen(GameLogScreen::new);
    }

    public static void showCardScriptScreen(final MagicCardDefinition card) {
        showScreen(() -> new CardScriptScreen(card));
    }

    public static void showDecksScreen(final IDeckConsumer deckConsumer) {
        showScreen(() -> new DecksScreen(deckConsumer));
    }

    public static void showDuelGameScreen(final MagicGame game) {
        showScreen(() -> new DuelGameScreen(game));
    }

    public static void showDuelGameScreen(final MagicDuel duel) {
        showScreen(() -> new DuelGameScreen(duel));
    }

    public static void showStartScreen() {
        screens.clear();
        showScreen(StartScreen::new);
    }

    public static void showImportScreen() {
        screens.clear();
        showScreen(ImportScreen::new);
    }

    public static void showAboutScreen() {
        if (screens.peek() instanceof AboutScreen) {
            // already open, do nothing
        } else {
            showScreen(AboutScreen::new);
            mainFrame.getGlassPane().setVisible(false);
        }
    }

    public static void showDownloadImagesScreen() {
        showScreen(DownloadImagesScreen::new);
    }

    public static void showPlayerScreen(String guid) {
        showScreen(() -> new PlayerScreen(guid));
    }

    public static boolean isDeckScreenShowing() {
        return !screens.isEmpty() && screens.peek() instanceof DeckScreen;
    }

    public static boolean isActive(MScreen aScreen) {
        return screens.peek() == aScreen;
    }
}
