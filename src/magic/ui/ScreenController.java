package magic.ui;

import magic.translate.UiString;
import magic.ui.utility.MagicStyle;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import magic.ui.dialog.DuelSidebarLayoutDialog;
import magic.ui.dialog.prefs.PreferencesDialog;
import magic.ui.widget.duel.choice.MulliganChoicePanel;
import magic.ui.screen.test.TestScreen;
import magic.ui.screen.MScreen;
import magic.ui.screen.duel.player.avatar.AvatarImagesScreen;
import magic.ui.screen.card.explorer.ExplorerScreen;
import magic.ui.screen.card.script.CardScriptScreen;
import magic.ui.screen.duel.player.zone.CardZoneScreen;
import magic.ui.screen.deck.editor.DeckEditorSplitScreen;
import magic.ui.screen.deck.editor.DeckEditorScreen;
import magic.ui.screen.deck.tiled.DeckTiledCardsScreen;
import magic.ui.screen.deck.DeckViewScreen;
import magic.ui.screen.decks.DecksScreen;
import magic.ui.screen.about.AboutScreen;
import magic.ui.screen.images.download.DownloadImagesScreen;
import magic.ui.screen.duel.decks.DuelDecksScreen;
import magic.ui.screen.duel.game.DuelGameScreen;
import magic.ui.screen.duel.game.log.GameLogScreen;
import magic.ui.screen.menu.help.HelpMenuScreen;
import magic.ui.screen.menu.migrate.ImportScreen;
import magic.ui.screen.keywords.KeywordsScreen;
import magic.ui.screen.menu.main.MainMenuScreen;
import magic.ui.screen.duel.mulligan.MulliganScreen;
import magic.ui.screen.duel.setup.NewDuelSettingsScreen;
import magic.ui.screen.readme.ReadmeScreen;
import magic.ui.screen.deck.hand.SampleHandScreen;
import magic.ui.screen.duel.player.SelectAiPlayerScreen;
import magic.ui.screen.duel.player.SelectHumanPlayerScreen;
import magic.ui.screen.menu.settings.SettingsMenuScreen;
import magic.ui.screen.menu.language.StartScreen;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.screen.menu.dev.DevMenuScreen;
import magic.utility.MagicSystem;

public final class ScreenController {

    // translatable strings.
    private static final String _S2 = "Information";
    private static final String _S3 = "Warning";

    private static MagicFrame mainFrame = null;
    private static final Stack<MScreen> screens = new Stack<>();
    private static MScreen hiddenScreen;

    public static MagicFrame getMainFrame() {
        if (mainFrame == null && java.awt.GraphicsEnvironment.isHeadless() == false) {
            mainFrame = new MagicFrame(MagicSystem.SOFTWARE_TITLE);
        }
        return mainFrame;
    }

    public static void showPreferencesDialog() {
        final PreferencesDialog dialog = new PreferencesDialog(getMainFrame(), isDuelActive());
        if (dialog.isRestartRequired()) {
            SwingUtilities.invokeLater(() -> {
                showMainMenuScreen();
            });
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
        if (getScreensStackSize() == 1) {
            mainFrame.quitToDesktop(isEscapeKeyAction);
        } else {
            doCloseActiveScreen();
        }
    }

    public static void closeActiveScreen() {
        closeActiveScreen(false);
    }

    private static void showScreen(MScreen screen) {
        if (hiddenScreen != null && hiddenScreen.getClass().getName().equals(screen.getClass().getName())) {
            screen = hiddenScreen;
            hiddenScreen = null;
        }

        setMainFrameScreen(screen);
        screens.push(screen);
        ((JPanel)screen).setVisible(true);
        ((JPanel)screen).requestFocus();
    }

    private static void setMainFrameScreen(final MScreen screen) {
        getMainFrame().setContentPanel((JPanel)screen);
    }

    public static int getScreensStackSize() {
        return screens.size();
    }

    public static JPanel getActiveScreen() {
        return (JPanel)screens.peek();
    }

    public static void refreshStyle() {
        for (MScreen screen : screens) {
            MagicStyle.refreshComponentStyle((JPanel)screen);
        }
    }

    public static void showInfoMessage(final String message) {
        JOptionPane.showMessageDialog(getMainFrame(), message, UiString.get(_S2), JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningMessage(final String message) {
        MagicSound.BEEP.play();
        JOptionPane.showMessageDialog(getMainFrame(), message, UiString.get(_S3), JOptionPane.WARNING_MESSAGE);
    }

    public static void showDuelSidebarDialog(final IUIGameController controller) {
        new DuelSidebarLayoutDialog(getMainFrame(), controller);
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
            ((JPanel)hiddenScreen).setVisible(false);
            showScreen(screens.pop());
        } else {
            throw new RuntimeException("A screen is already hidden - only one allowed at a time!");
        }
    }


    //
    // public show screens methods
    //

    public static void showDevMenuScreen() {
        showScreen(new DevMenuScreen());
    }

    public static void showTestScreen() {
        showScreen(new TestScreen());
    }

    public static void showDuelDecksScreen(final MagicDuel duel) {
        if (!screens.isEmpty() && screens.peek() instanceof DuelDecksScreen) {
            screens.pop();
        }
        showScreen(new DuelDecksScreen(duel));
    }

    public static void showMainMenuScreen() {
        screens.clear();
        showScreen(new MainMenuScreen());
    }

    public static void showReadMeScreen() {
        showScreen(new ReadmeScreen());
    }

    public static void showKeywordsScreen() {
        showScreen(new KeywordsScreen());
    }

    public static void showHelpMenuScreen() {
        showScreen(new HelpMenuScreen());
    }

    public static void showSettingsMenuScreen() {
        showScreen(new SettingsMenuScreen());
    }

    public static void showCardExplorerScreen() {
        showScreen(new ExplorerScreen());
    }

    public static void showDeckEditor(final MagicDeck deck) {
        showScreen(GeneralConfig.getInstance().isSplitViewDeckEditor()
                ? new DeckEditorSplitScreen(deck)
                : new DeckEditorScreen(deck));
    }

    public static void showDeckEditor() {
        showScreen(GeneralConfig.getInstance().isSplitViewDeckEditor()
                ? new DeckEditorSplitScreen()
                : new DeckEditorScreen());
    }

    public static void showDeckViewScreen(MagicDeck deck, MagicCardDefinition selectedCard) {
        showScreen(new DeckViewScreen(deck, selectedCard));
    }

    public static void showSampleHandScreen(final MagicDeck deck) {
        showScreen(new SampleHandScreen(deck));
    }

    public static void showCardZoneScreen(final MagicCardList cards, final String zoneName, final boolean animateCards) {
        showScreen(new CardZoneScreen(cards, zoneName, animateCards));
    }

    public static void showMulliganScreen(final MulliganChoicePanel choicePanel, final MagicCardList hand) {
        if (screens.peek() instanceof MulliganScreen) {
            final MulliganScreen screen = (MulliganScreen) screens.peek();
            screen.dealNewHand(choicePanel, hand);
        } else {
            showScreen(new MulliganScreen(choicePanel, hand));
        }
    }

    public static void showDeckTiledCardsScreen(final MagicDeck deck) {
        showScreen(new DeckTiledCardsScreen(deck));
    }

    public static void showSelectAiProfileScreen(final IPlayerProfileListener listener, final PlayerProfile profile) {
        showScreen(new SelectAiPlayerScreen(listener, profile));
    }

    public static void showSelectHumanPlayerScreen(final IPlayerProfileListener listener, final PlayerProfile profile) {
        showScreen(new SelectHumanPlayerScreen(listener, profile));
    }

    public static void showAvatarImagesScreen(final IAvatarImageConsumer consumer) {
        showScreen(new AvatarImagesScreen(consumer));
    }

    public static void showNewDuelSettingsScreen() {
        showScreen(new NewDuelSettingsScreen());
    }

    public static void showGameLogScreen() {
        showScreen(new GameLogScreen());
    }

    public static void showCardScriptScreen(final MagicCardDefinition card) {
        showScreen(new CardScriptScreen(card));
    }

    public static void showDecksScreen(final IDeckConsumer deckConsumer) {
        showScreen(new DecksScreen(deckConsumer));
    }

    public static void showDuelGameScreen(final MagicGame game) {
        showScreen(new DuelGameScreen(game));
    }

    public static void showDuelGameScreen(final MagicDuel duel) {
        showScreen(new DuelGameScreen(duel));
    }

    public static void showStartScreen() {
        screens.clear();
        showScreen(new StartScreen());
    }

    public static void showImportScreen() {
        screens.clear();
        showScreen(new ImportScreen());
    }

    public static void showAboutScreen() {
        if (screens.peek() instanceof AboutScreen) {
            // already open, do nothing
        } else {
            showScreen(new AboutScreen());
            getMainFrame().getGlassPane().setVisible(false);
        }
    }

    public static void showDownloadImagesScreen() {
        showScreen(new DownloadImagesScreen());
    }

}
