package magic.ui.screen.duel.game;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import magic.ai.MagicAI;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.exception.InvalidDeckException;
import magic.exception.UndoClickedException;
import magic.game.state.GameState;
import magic.game.state.GameStateFileWriter;
import magic.game.state.GameStateSnapshot;
import magic.model.IUIGameController;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicObject;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerZone;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.choice.MagicPlayChoice;
import magic.model.choice.MagicPlayChoiceResult;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPriorityEvent;
import magic.model.phase.MagicPhaseType;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;
import magic.translate.MText;
import magic.translate.StringContext;
import magic.ui.IChoiceViewer;
import magic.ui.IPlayerZoneListener;
import magic.ui.MagicFileChoosers;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.helpers.KeyEventAction;
import magic.ui.screen.duel.mulligan.MulliganScreen;
import magic.ui.widget.card.AnnotatedCardPanel;
import magic.ui.widget.duel.animation.DrawCardAnimation;
import magic.ui.widget.duel.animation.MagicAnimation;
import magic.ui.widget.duel.animation.MagicAnimations;
import magic.ui.widget.duel.animation.PlayCardAnimation;
import magic.ui.widget.duel.choice.ColorChoicePanel;
import magic.ui.widget.duel.choice.ManaCostXChoicePanel;
import magic.ui.widget.duel.choice.MayChoicePanel;
import magic.ui.widget.duel.choice.ModeChoicePanel;
import magic.ui.widget.duel.choice.MulliganChoicePanel;
import magic.ui.widget.duel.choice.MultiKickerChoicePanel;
import magic.ui.widget.duel.choice.PlayChoicePanel;
import magic.ui.widget.duel.sidebar.LogStackViewer;
import magic.ui.widget.duel.viewer.PlayerZoneViewer;
import magic.ui.widget.duel.viewer.UserActionPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;

public class SwingGameController implements IUIGameController {

    // translatable strings
    private static final String _S1 = "conceded";
    private static final String _S2 = "lost";
    @StringContext(eg = "Player1 conceded/lost the game.")
    private static final String _S3 = "%s %s the game.";
    private static final String _S4 = "You may pay the buyback %s.";
    @StringContext(eg = "get single kicker count choice")
    private static final String _S5 = "You may pay the %s %s.";
    private static final String _S6 = "You may take a mulligan.";

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final DuelLayeredPane duelPane;
    private final DuelPanel gamePanel;
    private LogStackViewer logStackViewer;

    private final MagicGame game;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean isPaused =  new AtomicBoolean(false);
    private final AtomicBoolean gameConceded = new AtomicBoolean(false);
    private final AtomicBoolean isStackFastForward = new AtomicBoolean(false);
    private final AtomicBoolean isPauseCancelled = new AtomicBoolean(false);
    private final Collection<IChoiceViewer> choiceViewers = new ArrayList<>();
    private Set<?> validChoices = Collections.emptySet();
    private AnnotatedCardPanel cardPopup;
    private UserActionPanel userActionPanel;
    private boolean actionClicked;
    private boolean combatChoice;
    private boolean resetGame;
    private MagicTarget choiceClicked = MagicTargetNone.getInstance();
    private MagicCardDefinition sourceCardDefinition = MagicCardDefinition.UNKNOWN;
    private final BlockingQueue<Boolean> input = new SynchronousQueue<>();
    private GameViewerInfo gameViewerInfo;
    private PlayerZoneViewer playerZoneViewer;
    private final List<IPlayerZoneListener> playerZoneListeners = new ArrayList<>();
    private MagicAnimation animation = null;

    private static boolean isControlKeyDown = false;
    private static final KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            isControlKeyDown = e.isControlDown();
            return false;
        }
    };

    public SwingGameController(final DuelLayeredPane aDuelPane, final MagicGame aGame) {
        this.duelPane = aDuelPane;
        this.game = aGame;
        gameViewerInfo = new GameViewerInfo(game);
        gamePanel = duelPane.getDuelPanel();
        gamePanel.setController(this);
        duelPane.getCardViewer().setController(this);
        clearValidChoices();

        setControlKeyMonitor();
        setKeyEventActions();
    }

    private void setKeyEventActions() {

        KeyEventAction.doAction(gamePanel, this::actionKeyPressed)
            .on(0, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);

        KeyEventAction.doAction(gamePanel, this::undoKeyPressed)
            .on(0, KeyEvent.VK_LEFT, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_DELETE);

        KeyEventAction.doAction(gamePanel, this::passKeyPressed)
            .on(InputEvent.SHIFT_MASK, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);

        KeyEventAction.doAction(gamePanel, this::switchPlayerZone)
            .on(0, KeyEvent.VK_S);

        KeyEventAction.doAction(gamePanel, this::showLogScreen)
            .on(0, KeyEvent.VK_L);

        KeyEventAction.doAction(gamePanel, this::showKeywordsScreen)
            .on(0, KeyEvent.VK_K);

        KeyEventAction.doAction(gamePanel, this::switchLogStackLayout)
            .on(0, KeyEvent.VK_M);

    }

    private void setControlKeyMonitor() {
        isControlKeyDown = false;
        final KeyboardFocusManager kbFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kbFocusManager.removeKeyEventDispatcher(keyEventDispatcher);
        kbFocusManager.addKeyEventDispatcher(keyEventDispatcher);
    }

    public MagicGame getGame() {
        return game;
    }

    @Override
    public void enableForwardButton() {
        SwingUtilities.invokeLater(() -> {
            userActionPanel.enableButton();
        });
    }

    @Override
    public void disableActionButton(final boolean thinking) {
        SwingUtilities.invokeLater(() -> {
            userActionPanel.disableButton(thinking);
        });
    }

    private void disableActionUndoButtons() {
        SwingUtilities.invokeLater(() -> {
            userActionPanel.disableButton(false);
            userActionPanel.enableUndoButton(true);
        });
    }

    @Override
    public void pause(final int t) {
        assert !SwingUtilities.isEventDispatchThread();
        disableActionUndoButtons();
        int tick = 0;
        while (tick < t && isPauseCancelled.get() == false) {
            try {
                Thread.sleep(10);
            } catch (final InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            tick += 10;
        }
        isPauseCancelled.set(false);
    }

    private static void invokeAndWait(final Runnable task) {
        try { //invoke and wait
            SwingUtilities.invokeAndWait(task);
        } catch (final InterruptedException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void waitForUIUpdates() {
        invokeAndWait(() -> {
            //do nothing, ensure that event dispatch queue is cleared
        });
    }

    /** Returns true when undo was clicked. */
    private boolean waitForInputOrUndo() {
        try {
            waitForUIUpdates();
            input.clear();
            return input.take();
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void waitForInput() throws UndoClickedException {
        final boolean undoClicked = waitForInputOrUndo();
        if (undoClicked) {
            throw new UndoClickedException();
        }
    }

    private <E extends JComponent> E waitForInput(final Callable<E> func) throws UndoClickedException {
        final AtomicReference<E> ref = new AtomicReference<>();
        final AtomicReference<Exception> except = new AtomicReference<>();
        SwingUtilities.invokeLater(() -> {
            try {
                final E content = func.call();
                ref.set(content);
                userActionPanel.setContentPanel(content);
            } catch (Exception ex) {
                except.set(ex);
            }
        });
        waitForInput();
        if (except.get() != null) {
            throw new RuntimeException(except.get());
        } else {
            return ref.get();
        }
    }

    private void resume(final boolean undoClicked) {
        input.offer(undoClicked);
    }

    public void switchPlayerZone() {
        playerZoneViewer.switchPlayerZone();
    }

    public void passKeyPressed() {
        if (gamePanel.canClickAction()) {
            actionClicked();
            game.skipTurnTill(MagicPhaseType.EndOfTurn);
        }
    }

    public void actionKeyPressed() {
        if (duelPane.getDialogPanel().isVisible()) {
            duelPane.getDialogPanel().setVisible(false);
        } else if (gamePanel.canClickAction()) {
            actionClicked();
        }
    }

    public void actionClicked() {
        hideInfo();
        userActionPanel.clearContentPanel();
        actionClicked = true;
        choiceClicked = MagicTargetNone.getInstance();
        resume(false);
    }

    public void undoKeyPressed() {
        if (gamePanel.canClickUndo()) {
            undoClicked();
        }
    }

    public void undoClicked() {
        hideInfo();
        if (game.hasUndoPoints()) {
            actionClicked = false;
            choiceClicked = MagicTargetNone.getInstance();
            setSourceCardDefinition(MagicSource.NONE);
            clearValidChoices();
            resume(true);
        }
    }

    public void processClick(final MagicTarget choice) {
        if (validChoices.contains(choice)) {
            actionClicked = false;
            choiceClicked = choice;
            hideInfo();
            resume(false);
        }
    }

    @Override
    public boolean isActionClicked() {
        return actionClicked;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getChoiceClicked() {
        return (T)choiceClicked;
    }

    public void setImageCardViewer(final AnnotatedCardPanel cardViewer) {
        this.cardPopup = cardViewer;
    }

    public void setUserActionPanel(final UserActionPanel userActionPanel) {
        this.userActionPanel = userActionPanel;
    }

    private void viewCardPopupCentered(CardViewerInfo cardInfo, final int popupDelay) {

        final Rectangle containerZone = gamePanel.getBattlefieldPanelBounds();

        // set popup image and size.
        cardPopup.setCard(cardInfo, containerZone.getSize());

        final int x = containerZone.x + (int)((containerZone.getWidth() / 2) - (cardPopup.getWidth() / 2));
        final int y = containerZone.y + (int)((containerZone.getHeight() / 2) - (cardPopup.getHeight() / 2));
        cardPopup.setLocation(x,y);

        cardPopup.showDelayed(popupDelay);
    }

    public void viewCardPopupCentered(final MagicObject cardObject, final int popupDelay) {

        // mouse wheel rotation event can fire more than once
        // so ignore all but the first event.
        if (cardObject == cardPopup.getMagicObject()) {
            return;
        }

        // ignore if user wants current popup to remain open
        // so they can view ability icon tooltips.
        if (isControlKeyDown && cardPopup.isVisible()) {
            return;
        }

        final Rectangle containerZone = gamePanel.getBattlefieldPanelBounds();

        // set popup image and size.
        cardPopup.setCard(cardObject, containerZone.getSize());

        final int x = containerZone.x + (int)((containerZone.getWidth() / 2) - (cardPopup.getWidth() / 2));
        final int y = containerZone.y + (int)((containerZone.getHeight() / 2) - (cardPopup.getHeight() / 2));
        cardPopup.setLocation(x,y);

        cardPopup.showDelayed(popupDelay);
    }

    /**
     *
     * @param cardObject
     * @param index
     * @param cardRect : screen position & size of selected card on battlefield.
     * @param popupAboveBelowOnly : if true then the popup will restrict its height to always fit above/below the selected card.
     */
    public void viewCardPopup(
        final MagicObject cardObject,
        final Rectangle cardRect,
        final boolean popupAboveBelowOnly,
        final int popupDelay) {

        // mouse wheel rotation event can fire more than once
        // so ignore all but the first event.
        if (cardObject == cardPopup.getMagicObject()) {
            return;
        }

        // ignore if user wants current popup to remain open
        // so they can view ability icon tooltips.
        if (isControlKeyDown && cardPopup.isVisible()) {
            return;
        }

        final boolean isAutoPopup = !CONFIG.isMouseWheelPopup();
        final int VERTICAL_INSET = 4; // pixels
        final int PAD2 = 0;

        final Dimension gamePanelSize = gamePanel.getSize();

        // update selected card position so it is relative to container instead of screen.
        final Point gamePanelScreenPosition = gamePanel.getLocationOnScreen();
        cardRect.x -= gamePanelScreenPosition.x;
        cardRect.y -= gamePanelScreenPosition.y;

        // set popup image and size.
        if (popupAboveBelowOnly) {
            final int spaceAbove = cardRect.y;
            final int spaceBelow = gamePanelSize.height - cardRect.y - cardRect.height;
            final int height = Math.max(spaceAbove, spaceBelow)  - (VERTICAL_INSET * 2);
            cardPopup.setCard(cardObject, new Dimension(gamePanelSize.width, height));
        } else {
            cardPopup.setCard(cardObject, gamePanelSize);
        }

        final int popupWidth = cardPopup.getWidth();
        final int popupHeight = cardPopup.getHeight();

        int x = cardRect.x + (cardRect.width - popupWidth) / 2;
        final int y1 = cardRect.y - popupHeight - VERTICAL_INSET;
        final int y2 = cardRect.y + cardRect.height + VERTICAL_INSET;
        final int dy2 = gamePanelSize.height - y2 - popupHeight;
        if (x + popupWidth >= gamePanelSize.width) {
            x = cardRect.x + cardRect.width - popupWidth;
        }
        int y;
        // Position is next to card?
        if (y1 < PAD2 && dy2 < PAD2) {
            if (isAutoPopup) {
                x = cardRect.x - popupWidth - VERTICAL_INSET;
            } else {
                x = cardRect.x - popupWidth + cardRect.width;
            }
            if (x < 0) {
                if (isAutoPopup) {
                    x = cardRect.x + cardRect.width + VERTICAL_INSET;
                } else {
                    x = cardRect.x;
                }
            }
            if (y1 >= dy2) {
                y = cardRect.y + cardRect.height - popupHeight;
                if (y < PAD2) {
                    y = PAD2;
                }
            } else {
                y = cardRect.y;
                if (y + popupHeight + PAD2 > gamePanelSize.height) {
                    y = gamePanelSize.height - PAD2 - popupHeight;
                }
            }
        // Position is above card?
        } else if (y1 >= PAD2) {
            y = y1;
        // Position if beneath card.
        } else {
            y = y2;
        }

        cardPopup.setLocation(x,y);
        cardPopup.showDelayed(popupDelay);
    }

    public void viewCardPopup(final MagicObject cardObject, final Rectangle cardRect, final boolean popupAboveBelowOnly) {
        viewCardPopup(cardObject, cardRect, popupAboveBelowOnly, getPopupDelay());
    }

    public boolean isPopupVisible() {
        return cardPopup.isVisible();
    }

    /**
     *
     * @param cardObject
     * @param cardRect : screen position & size of selected card on battlefield.
     */
    public void viewCardPopup(final MagicObject cardObject, final Rectangle cardRect) {
        viewCardPopup(cardObject, cardRect, false);
    }

    public void viewInfoRight(final MagicCardDefinition cardDefinition,final int index,final Rectangle rect) {
        final Dimension gamePanelSize = gamePanel.getSize();
        // update rect position so it is relative to container instead of screen.
        final Point pointOnScreen = gamePanel.getLocationOnScreen();
        rect.x -= pointOnScreen.x;
        rect.y -= pointOnScreen.y;
        final int x = rect.x + rect.width + 10;
        cardPopup.setCardForPrompt(cardDefinition, gamePanelSize);
        final int maxY = gamePanelSize.height - cardPopup.getHeight();
        int y = rect.y + (rect.height-cardPopup.getHeight()) / 2;
        if (y < 0) {
            y = 0;
        } else if (y > maxY) {
            y = maxY;
        }
        cardPopup.setLocation(x,y);
        cardPopup.showDelayed(getPopupDelay());
    }

    private int getPopupDelay() {
        return CONFIG.isMouseWheelPopup() ? 0 : CONFIG.getPopupDelay();
    }

    public void hideInfo() {
        if (!isControlKeyDown) {
            cardPopup.hideDelayed();
        }
    }

    public void hideInfoNoDelay() {
        if (!isControlKeyDown) {
            cardPopup.hideNoDelay();
        }
    }

    @Override
    public void setSourceCardDefinition(final MagicSource source) {
        sourceCardDefinition=source.getCardDefinition();
    }

    public MagicCardDefinition getSourceCardDefinition() {
        return sourceCardDefinition;
    }

    @Override
    public void focusViewers(final int handGraveyard) {
        SwingUtilities.invokeLater(() -> {
            gamePanel.focusViewers(handGraveyard);
        });
    }

    @Override
    public void clearCards() {
        showCards(new MagicCardList());
    }

    @Override
    public void showCards(final MagicCardList cards) {
        SwingUtilities.invokeLater(() -> {
            gamePanel.showCards(cards);
        });
    }

    public void registerChoiceViewer(final IChoiceViewer choiceViewer) {
        choiceViewers.add(choiceViewer);
    }

    private void showValidChoices() {
        assert SwingUtilities.isEventDispatchThread();
        for (final IChoiceViewer choiceViewer : choiceViewers) {
            choiceViewer.showValidChoices(validChoices);
        }
    }

    public boolean isCombatChoice() {
        return combatChoice;
    }

    @Override
    public void clearValidChoices() {
        // called from both edt and application threads.
        SwingUtilities.invokeLater(() -> {
            clearDisplayedValidChoices();
        });
        showMessage(MagicSource.NONE, "");
    }

    private void clearDisplayedValidChoices() {
        assert SwingUtilities.isEventDispatchThread();
        if (!validChoices.isEmpty()) {
            validChoices.clear();
            combatChoice=false;
            showValidChoices();
        }
    }

    @Override
    public void setValidChoices(final Set<?> aValidChoices, final boolean aCombatChoice) {
        assert !SwingUtilities.isEventDispatchThread();
        SwingUtilities.invokeLater(() -> {
            clearDisplayedValidChoices();
            validChoices = new HashSet<>(aValidChoices);
            combatChoice = aCombatChoice;
            showValidChoices();
        });
    }

    public Set<?> getValidChoices() {
        return validChoices;
    }

    public GameViewerInfo getGameViewerInfo() {
        return gameViewerInfo;
    }

//    private void debugAnimation(MagicAnimation animation) {
//        if (animation != null) {
//            MagicAnimations.debugPrint(animation);
////            pause(2000);
//        }
//    }

    private void doFlashPlayerZoneButton(GameViewerInfo newGameInfo) {
        if (animation instanceof PlayCardAnimation) {
            gamePanel.doFlashPlayerHandZoneButton(newGameInfo.getTurnPlayer());
        } else if (animation instanceof DrawCardAnimation) {
            gamePanel.doFlashLibraryZoneButton(newGameInfo.getTurnPlayer());
        }
    }

    private boolean isReadyToAnimate() {
        return CONFIG.showGameplayAnimations() && (animation == null || animation.isRunning.get() == false);
    }

    private void doPlayAnimationAndWait(final GameViewerInfo oldGameInfo, final GameViewerInfo newGameInfo) {
        if (isReadyToAnimate() == false) {
            return;
        }

        // skip animation when newGameInfo is result of undo
        if (newGameInfo.getUndoPoints() < oldGameInfo.getUndoPoints()) {
            return;
        }

        animation = MagicAnimations.getGameplayAnimation(oldGameInfo, newGameInfo, gamePanel);
        if (animation != null) {
            animation.isRunning.set(true);
            SwingUtilities.invokeLater(() -> {
                doFlashPlayerZoneButton(newGameInfo);
                duelPane.getAnimationPanel().playAnimation(animation);
            });
            while (animation.isRunning.get() == true) {
                pause(100);
            }
        }
    }

    /**
     * Update/render the gui based on the model state.
     */
    @Override
    public void updateGameView() {

        assert !SwingUtilities.isEventDispatchThread();

        final GameViewerInfo oldGameInfo = gameViewerInfo;
        gameViewerInfo = new GameViewerInfo(game);

        doPlayAnimationAndWait(oldGameInfo, gameViewerInfo);

        SwingUtilities.invokeLater(() -> {
            gamePanel.update(gameViewerInfo);
        });

        waitForUIUpdates();
    }

    public static String getMessageWithSource(final MagicSource source,final String message) {
        if (source == null) {
            throw new RuntimeException("source is null");
        }
        if (source == MagicSource.NONE) {
            return message;
        } else {
            return "("+source+")|"+message;
        }
    }

    @Override
    public void showMessage(final MagicSource source, final String message) {
        SwingUtilities.invokeLater(() -> {
            userActionPanel.showMessage(getMessageWithSource(source, MText.get(message)));
        });
    }

    private Object[] getArtificialNextEventChoiceResults(final MagicEvent event) {
        disableActionButton(true);
        if (CONFIG.getHideAiActionPrompt()) {
            showMessage(MagicSource.NONE, "");
        } else {
            showMessage(event.getSource(),event.getChoiceDescription());
        }
        waitForUIUpdates();

        //dynamically get the AI based on the player's index
        final MagicPlayer player = event.getPlayer();
        final MagicAI ai = player.getAiProfile().getAiType().getAI();
        return ai.findNextEventChoiceResults(game, player);
    }

    private Object[] getPlayerNextEventChoiceResults(final MagicEvent event) throws UndoClickedException {
        setSourceCardDefinition(event.getSource());
        final Object[] choiceResults;
        try {
            choiceResults = event.getChoice().getPlayerChoiceResults(this,game,event);
        } finally {
            clearValidChoices();
            setSourceCardDefinition(MagicSource.NONE);
        }
        return choiceResults;
    }

    private void executeNextEventWithChoices(final MagicEvent event) {
        final Object[] choiceResults;
        if (event.getPlayer().isArtificial()) {
            choiceResults = getArtificialNextEventChoiceResults(event);
            // clear skip till EOT if AI plays something
            if (event.getChoice() == MagicPlayChoice.getInstance() && choiceResults[0] != MagicPlayChoiceResult.PASS && choiceResults[0] != MagicPlayChoiceResult.SKIP) {
                game.clearSkipTurnTill();
            }
        } else {
            try {
                choiceResults = getPlayerNextEventChoiceResults(event);
            } catch (UndoClickedException undo) {
                if (gameConceded.get()) {
                    return;
                } else {
                    performUndo();
                    return;
                }
            }
        }
        game.executeNextEvent(choiceResults);
    }

    public void resetGame() {
        if (game.hasUndoPoints()) {
            resetGame=true;
            undoClicked();
        }
    }

    public void concede() {
        if (!gameConceded.get() && !game.isFinished()) {
            game.setLosingPlayer(game.getPlayer(0));
            game.setConceded(true);
            game.clearUndoPoints();
            gameConceded.set(true);
            resume(true);
        }
    }

    private void performUndo() {
        if (resetGame) {
            resetGame=false;
            while (game.hasUndoPoints()) {
                game.restore();
            }
        } else {
            game.restore();
        }
    }

    @Override
    public void haltGame() {
        running.set(false);
    }

    /**
     * Main game loop runs on separate thread.
     */
    @Override
    public void runGame() {
        assert !SwingUtilities.isEventDispatchThread();
        running.set(true);
        while (running.get()) {
            if (isPaused.get()) {
                pause(100);
            } else if (game.isFinished()) {
                doNextActionOnGameFinished();
            } else {
                executeNextEventOrPhase();
                updateGameView();
            }
        }
    }

    /**
     * Once a game has finished determine what happens next.
     * <p>
     * If running an automated game then automatically start next game/duel.
     * If an interactive game then wait for input from user.
     */
    private void doNextActionOnGameFinished() {
        game.logMessages();
        clearValidChoices();
        showEndGameMessage();
        playEndGameSoundEffect();
        enableForwardButton();
        if (MagicSystem.isAiVersusAi() == false && waitForInputOrUndo()) {
            performUndo();
            updateGameView();
        } else {
            game.advanceDuel();
            SwingUtilities.invokeLater(() -> {
                try {
                    gamePanel.close();
                } catch (InvalidDeckException ex) {
                    ScreenController.showWarningMessage(ex.getMessage());
                }
            });
            running.set(false);
        }
    }

    private void executeNextEventOrPhase() {
        if (game.hasNextEvent()) {
            executeNextEvent();
        } else {
            game.executePhase();
        }
    }

    private void executeNextEvent() {
        final MagicEvent event=game.getNextEvent();
        if (event instanceof MagicPriorityEvent) {
            game.logMessages();
        }
        if (event.hasChoice()) {
            executeNextEventWithChoices(event);
        } else {
            game.executeNextEvent();
        }
    }

    private void showEndGameMessage() {
        assert !SwingUtilities.isEventDispatchThread();
        if (!MagicSystem.isAiVersusAi() && !MagicSystem.isDebugMode()) {
            SwingUtilities.invokeLater(() -> {
                duelPane.getDialogPanel().showEndGameMessage(gameViewerInfo);
            });
        }
        showMessage(MagicSource.NONE,
            String.format("{L} %s",
                MText.get(_S3,
                    game.getLosingPlayer(),
                    gameConceded.get() ? MText.get(_S1) : MText.get(_S2)
                )
            )
        );
    }

    private void playEndGameSoundEffect() {
        if (game.getLosingPlayer().getIndex() == 0) {
            game.playSound(MagicSound.LOSE);
        } else {
            game.playSound(MagicSound.WIN);
        }
    }

    public void showChoiceCardPopup() {
        final MagicCardDefinition cardDefinition = getSourceCardDefinition();
        if (cardDefinition != MagicCardDefinition.UNKNOWN) {
            final Point point = userActionPanel.getLocationOnScreen();
            viewInfoRight(cardDefinition, 0, new Rectangle(point.x, point.y-20, userActionPanel.getWidth(), userActionPanel.getHeight()));
        }
    }

    /**
     * devMode only currently.
     */
    public void doSaveGame() {
        if (isValidSaveState()) {
            final File saveGameFile = MagicFileChoosers.getSaveGameFile(gamePanel);
            if (saveGameFile != null) {
                final GameState gameState = GameStateSnapshot.getGameState(game);
                GameStateFileWriter.createSaveGameFile(gameState, saveGameFile.getName());
                ScreenController.showInfoMessage("Game saved!");
            }
        } else {
            ScreenController.showInfoMessage("Can not save game state at this time.");
        }
    }

    public void createGameplayReport() {
        setGamePaused(true);
        try {
            GameplayReport.createNewReport(game);
        } catch (Exception ex) {
            Logger.getLogger(GameplayReport.class.getName()).log(Level.WARNING, null, ex);
            ScreenController.showWarningMessage("There was a problem creating the report :-\n\n" + ex.getMessage());
        }

        try {
            GameplayReport.openReportDirectory();
        } catch (Exception ex) {
            Logger.getLogger(GameplayReport.class.getName()).log(Level.WARNING, null, ex);
            ScreenController.showWarningMessage(
                "There was a problem opening the reports folder at " +
                MagicFileSystem.getDataPath(MagicFileSystem.DataPath.REPORTS).toString() +
                " :-\n\n" + ex.getMessage()
            );
        }

        setGamePaused(false);
    }

    private boolean isValidSaveState() {
        final boolean isHumanTurn = game.getTurnPlayer().isHuman();
        final boolean isHumanPriority = game.getPriorityPlayer().isHuman();
        final boolean isStackEmpty = game.getStack().isEmpty();
        final boolean isFirstMain = game.isPhase(MagicPhaseType.FirstMain);
        return isHumanTurn && isHumanPriority && isFirstMain && isStackEmpty;
    }

    public void setGamePaused(final boolean isPaused) {
        this.isPaused.set(isPaused);
    }

    @Override
    public MagicSubType getLandSubTypeChoice(final MagicSource source) throws UndoClickedException {
        final ColorChoicePanel choicePanel = waitForInput(new Callable<ColorChoicePanel>() {
            @Override
            public ColorChoicePanel call() {
                return new ColorChoicePanel(SwingGameController.this, source);
            }
        });
        return choicePanel.getColor().getLandSubType();
    }

    @Override
    public boolean getPayBuyBackCostChoice(final MagicSource source, final String costText) throws UndoClickedException {
        final MayChoicePanel kickerPanel = waitForInput(new Callable<MayChoicePanel>() {
            @Override
            public MayChoicePanel call() {
                return new MayChoicePanel(
                    SwingGameController.this,
                    source,
                    MText.get(_S4, costText)
                );
            }
        });
        return kickerPanel.isYesClicked();
    }

    @Override
    public MagicColor getColorChoice(final MagicSource source) throws UndoClickedException {
        final ColorChoicePanel choicePanel = waitForInput(new Callable<ColorChoicePanel>() {
            @Override
            public ColorChoicePanel call() {
                return new ColorChoicePanel(SwingGameController.this, source);
            }
        });
        return choicePanel.getColor();
    }

    @Override
    public int getMultiKickerCountChoice(final MagicSource source, final MagicManaCost cost, final int maximumCount, final String name) throws UndoClickedException {
        final MultiKickerChoicePanel kickerPanel = waitForInput(new Callable<MultiKickerChoicePanel>() {
            @Override
            public MultiKickerChoicePanel call() {
                return new MultiKickerChoicePanel(SwingGameController.this, source, cost, maximumCount, name);
            }
        });
        return kickerPanel.getKicker();
    }

    @Override
    public int getSingleKickerCountChoice(final MagicSource source, final MagicManaCost cost, final String name) throws UndoClickedException {
        final MayChoicePanel kickerPanel = waitForInput(new Callable<MayChoicePanel>() {
            @Override
            public MayChoicePanel call() {
                return new MayChoicePanel(
                        SwingGameController.this,
                        source,
                        MText.get(_S5, name, cost.getText()));
            }
        });
        return kickerPanel.isYesClicked() ? 1 : 0;
    }

    @Override
    public boolean getMayChoice(final MagicSource source, final String description) throws UndoClickedException {
        final MayChoicePanel choicePanel = waitForInput(new Callable<MayChoicePanel>() {
            @Override
            public MayChoicePanel call() {
                return new MayChoicePanel(SwingGameController.this, source, description);
            }
        });
        return choicePanel.isYesClicked();
    }

    @Override
    public boolean getTakeMulliganChoice(final MagicSource source, final MagicPlayer player) throws UndoClickedException {
        final MayChoicePanel choicePanel = waitForInput(new Callable<MayChoicePanel>() {
            @Override
            public MayChoicePanel call() {
                final boolean showMulliganScreen =
                        MulliganScreen.isActive() ||
                        (player.getHandSize() == DuelConfig.getInstance().getHandSize() &&
                         GeneralConfig.getInstance().showMulliganScreen());
                if (showMulliganScreen) {
                    return new MulliganChoicePanel(SwingGameController.this, source, MText.get(_S6), player.getPrivateHand());
                } else {
                    return new MayChoicePanel(SwingGameController.this, source, MText.get(_S6));
                }
            }
        });
        return choicePanel.isYesClicked();
    }

    @Override
    public int getModeChoice(final MagicSource source, final List<Integer> availableModes) throws UndoClickedException {
        final ModeChoicePanel choicePanel = waitForInput(new Callable<ModeChoicePanel>() {
            @Override
            public ModeChoicePanel call() {
                return new ModeChoicePanel(SwingGameController.this, source, availableModes);
            }
        });
        return choicePanel.getMode();
    }

    @Override
    public int getPayManaCostXChoice(final MagicSource source, final int maximumX) throws UndoClickedException {
        final ManaCostXChoicePanel choicePanel = waitForInput(new Callable<ManaCostXChoicePanel>() {
            @Override
            public ManaCostXChoicePanel call() {
                return new ManaCostXChoicePanel(SwingGameController.this, source, maximumX);
            }
        });
        return choicePanel.getValueForX();
    }

    @Override
    public MagicPlayChoiceResult getPlayChoice(final MagicSource source, final List<MagicPlayChoiceResult> results) throws UndoClickedException {
        final PlayChoicePanel choicePanel = waitForInput(new Callable<PlayChoicePanel>() {
            @Override
            public PlayChoicePanel call() {
                return new PlayChoicePanel(SwingGameController.this, source, results);
            }
        });
        return choicePanel.getResult();
    }

    public PlayerZoneViewer getPlayerZoneViewer() {
        if (playerZoneViewer == null) {
            playerZoneViewer = new PlayerZoneViewer(this);
        }
        return playerZoneViewer;
    }

    public void addPlayerZoneListener(final IPlayerZoneListener listener) {
        playerZoneListeners.add(listener);
    }

    public void notifyPlayerZoneChanged(final PlayerViewerInfo playerInfo, final MagicPlayerZone zone) {
        for (IPlayerZoneListener listener : playerZoneListeners) {
            listener.setActivePlayerZone(playerInfo, zone);
        }
    }

    @Override
    public void refreshSidebarLayout() {
        gamePanel.refreshSidebarLayout();
    }

    public void highlightCard(long magicCardId, boolean b) {
        if (magicCardId > 0) {
            final CardViewerInfo cardInfo = gameViewerInfo.getCardViewerInfo(magicCardId);
            if (cardInfo.isNotEmpty()) {
                gamePanel.highlightCard(cardInfo, b);
            } else {
                System.err.printf("Highlight failed! MagicCard #%d not found!\n", magicCardId);
            }
        }
    }

    public void showMagicCardImage(long magicCardId) {
        if (magicCardId > 0) {
            final CardViewerInfo cardInfo = gameViewerInfo.getCardViewerInfo(magicCardId);
            if (cardInfo.isNotEmpty()) {
                viewCardPopupCentered(cardInfo, 0);
            } else {
                System.err.printf("Highlight failed! MagicCard #%d not found!\n", magicCardId);
            }
        }
    }

    private void showLogScreen() {
        ScreenController.showGameLogScreen();
    }

    private void showKeywordsScreen() {
        ScreenController.showKeywordsScreen();
    }

    private void switchLogStackLayout() {
        logStackViewer.switchLogVisibility();
    }

    public void setLogStackViewer(LogStackViewer logStackViewer) {
        this.logStackViewer = logStackViewer;
    }

    public void showGameOptionsOverlay() {
        if (duelPane == null) {
            //do nothing
        } else if (duelPane.getDialogPanel().isVisible()) {
            duelPane.getDialogPanel().setVisible(false);
        } else {
            new GameOptionsOverlay(this);
        }
    }

    public void setStackCount(int count) {
        logStackViewer.setStackCount(count);
    }

    private int getStackItemPause() {
        return isStackFastForward.get() == true ? 0 : CONFIG.getMessageDelay();
    }

    @Override
    public void setStackFastForward(boolean b) {
        isPauseCancelled.set(b);
        isStackFastForward.set(b);
    }

    @Override
    public boolean isStackFastForward() {
        return isStackFastForward.get();
    }

    @Override
    public void doStackItemPause() {
        if (game.getStack().hasItem()) {
            if (getStackItemPause() > 0) {
                pause(getStackItemPause());
            }
        }
    }

    boolean waitingForUser() {
        return userActionPanel.isActionEnabled();
    }
}
