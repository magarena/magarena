package magic.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
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
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import magic.ai.MagicAI;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.data.SoundEffects;
import magic.exception.InvalidDeckException;
import magic.exception.UndoClickedException;
import magic.game.state.GameState;
import magic.game.state.GameStateFileWriter;
import magic.game.state.GameStateSnapshot;
import magic.model.ILogBookListener;
import magic.model.IUIGameController;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicLogBookEvent;
import magic.model.MagicManaCost;
import magic.model.MagicObject;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerZone;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.choice.MagicPlayChoiceResult;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPriorityEvent;
import magic.model.phase.MagicMainPhase;
import magic.model.phase.MagicPhaseType;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;
import magic.ui.card.AnnotatedCardPanel;
import magic.ui.duel.DuelPanel;
import magic.ui.duel.choice.ColorChoicePanel;
import magic.ui.duel.choice.ManaCostXChoicePanel;
import magic.ui.duel.choice.MayChoicePanel;
import magic.ui.duel.choice.ModeChoicePanel;
import magic.ui.duel.choice.MulliganChoicePanel;
import magic.ui.duel.choice.MultiKickerChoicePanel;
import magic.ui.duel.choice.PlayChoicePanel;
import magic.ui.duel.viewer.ChoiceViewer;
import magic.ui.duel.viewer.PlayerViewerInfo;
import magic.ui.duel.viewer.PlayerZoneViewer;
import magic.ui.duel.viewer.UserActionPanel;
import magic.ui.duel.viewer.ViewerInfo;
import magic.ui.screen.MulliganScreen;
import magic.utility.MagicSystem;

public class SwingGameController implements IUIGameController, ILogBookListener {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final DuelPanel gamePanel;
    private final MagicGame game;
    private final boolean selfMode = MagicSystem.isAiVersusAi();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean isPaused =  new AtomicBoolean(false);
    private final AtomicBoolean gameConceded = new AtomicBoolean(false);
    private final Collection<ChoiceViewer> choiceViewers = new ArrayList<>();
    private Set<?> validChoices = Collections.emptySet();
    private AnnotatedCardPanel cardPopup;
    private UserActionPanel userActionPanel;
    private boolean actionClicked;
    private boolean combatChoice;
    private boolean resetGame;
    private MagicTarget choiceClicked = MagicTargetNone.getInstance();
    private MagicCardDefinition sourceCardDefinition = MagicCardDefinition.UNKNOWN;
    private final BlockingQueue<Boolean> input = new SynchronousQueue<>();
    private int gameTurn = 0;
    private final ViewerInfo viewerInfo;
    private PlayerZoneViewer playerZoneViewer;
    private final List<IPlayerZoneListener> playerZoneListeners = new ArrayList<>();
    
    private static boolean isControlKeyDown = false;
    private static final KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            isControlKeyDown = e.isControlDown();
            return false;
        }
    };

    public SwingGameController(final DuelPanel aGamePanel,final MagicGame aGame) {
        gamePanel = aGamePanel;
        game = aGame;
        clearValidChoices();
        if (!CONFIG.isLogViewerDisabled()) {
            game.getLogBook().addListener(this);
        }
        viewerInfo = new ViewerInfo(game);

        setControlKeyMonitor();
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userActionPanel.enableButton(IconImages.getIcon(MagicIcon.FORWARD));
            }
        });
    }

    @Override
    public void disableActionButton(final boolean thinking) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userActionPanel.disableButton(thinking);
            }
        });
    }

    private void disableActionUndoButtons() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userActionPanel.disableButton(false);
                userActionPanel.enableUndoButton(true);
            }
        });
    }

    @Override
    public void pause(final int t) {
        disableActionUndoButtons();
        try { //sleep
            Thread.sleep(t);
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }


    private static void invokeAndWait(final Runnable task) {
        try { //invoke and wait
            SwingUtilities.invokeAndWait(task);
        } catch (final InterruptedException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Returns true when undo was clicked. */
    private boolean waitForInputOrUndo() {
        try {
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

    private void resume(final boolean undoClicked) {
        input.offer(undoClicked);
    }
    
    public void switchKeyPressed() {
        playerZoneViewer.switchPlayerZone();
    }

    public void passKeyPressed() {
        if (gamePanel.canClickAction()) {
            actionClicked();
            game.skipTurnTill(MagicPhaseType.Cleanup);
        }
    }

    public void actionKeyPressed() {
        if (gamePanel.canClickAction()) {
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
            setSourceCardDefinition(MagicEvent.NO_SOURCE);
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

    /**
     *
     * @param cardObject
     * @param index
     * @param cardRect : screen position & size of selected card on battlefield.
     * @param popupAboveBelowOnly : if true then the popup will restrict its height to always fit above/below the selected card.
     */
    public void viewCardPopup(
            final MagicObject cardObject,
            final int index,
            final Rectangle cardRect,
            final boolean popupAboveBelowOnly) {

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
        cardPopup.showDelayed(getPopupDelay());
    }

    public boolean isPopupVisible() {
        return cardPopup.isVisible();
    }

    /**
     *
     * @param cardObject
     * @param index
     * @param cardRect : screen position & size of selected card on battlefield.
     */
    public void viewCardPopup(final MagicObject cardObject, final int index, final Rectangle cardRect) {
        viewCardPopup(cardObject, index, cardRect, false);
    }

    public void viewInfoRight(final MagicCardDefinition cardDefinition,final int index,final Rectangle rect) {
        final Dimension gamePanelSize = gamePanel.getSize();
        // update rect position so it is relative to container instead of screen.
        final Point pointOnScreen = gamePanel.getLocationOnScreen();
        rect.x -= pointOnScreen.x;
        rect.y -= pointOnScreen.y;
        final int x = rect.x + rect.width + 10;
        cardPopup.setCard(cardDefinition, gamePanelSize);
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

    @Override
    public void setSourceCardDefinition(final MagicSource source) {
        sourceCardDefinition=source.getCardDefinition();
    }

    public MagicCardDefinition getSourceCardDefinition() {
        return sourceCardDefinition;
    }

    @Override
    public void focusViewers(final int handGraveyard) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gamePanel.focusViewers(handGraveyard);
            }
        });
    }

    @Override
    public void clearCards() {
        showCards(new MagicCardList());
    }

    @Override
    public void showCards(final MagicCardList cards) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gamePanel.showCards(cards);
            }
        });
    }

    public void registerChoiceViewer(final ChoiceViewer choiceViewer) {
        choiceViewers.add(choiceViewer);
    }

    private void showValidChoices() {
        assert SwingUtilities.isEventDispatchThread();
        for (final ChoiceViewer choiceViewer : choiceViewers) {
            choiceViewer.showValidChoices(validChoices);
        }
    }

    public boolean isCombatChoice() {
        return combatChoice;
    }

    @Override
    public void clearValidChoices() {
        // called from both edt and application threads.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                clearDisplayedValidChoices();
            }
        });
    }
    
    private void clearDisplayedValidChoices() {
        assert SwingUtilities.isEventDispatchThread();
        if (!validChoices.isEmpty()) {
            validChoices.clear();
            combatChoice=false;
            showValidChoices();
            showMessage(MagicEvent.NO_SOURCE, "");
        }
    }

    @Override
    public void setValidChoices(final Set<?> aValidChoices, final boolean aCombatChoice) {
        assert !SwingUtilities.isEventDispatchThread();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                clearDisplayedValidChoices();
                validChoices = new HashSet<>(aValidChoices);
                combatChoice = aCombatChoice;
                showValidChoices();
            }
        });
    }

    public Set<?> getValidChoices() {
        return validChoices;
    }

    public ViewerInfo getViewerInfo() {
        return viewerInfo;
    }

    /**
     * Update/render the gui based on the model state.
     */
    @Override
    public void updateGameView() {
        assert !SwingUtilities.isEventDispatchThread();

        // show New Turn notification (if appropriate & enabled).
        if (game.getTurn() != gameTurn) {
            gameTurn = game.getTurn();
            final boolean isShowingMulliganScreen = CONFIG.showMulliganScreen() && game.getTurn() == 1;
            if (!isShowingMulliganScreen && CONFIG.getNewTurnAlertDuration() > 0) {
                gamePanel.doNewTurnNotification(game);
            }
        }

        // Run before the view state is updated to reflect transition from old to new
        // model state. Should not return until animations have been completed or cancelled.
        gamePanel.runAnimation();

        // update game view DTO to reflect new model state.
        viewerInfo.update(game);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gamePanel.update();
            }
        });
    }

    public static String getMessageWithSource(final MagicSource source,final String message) {
        if (source == null) {
            throw new RuntimeException("source is null");
        }
        if (source == MagicEvent.NO_SOURCE) {
            return message;
        } else {
            return "("+source+")|"+message;
        }
    }

    @Override
    public void showMessage(final MagicSource source, final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userActionPanel.showMessage(getMessageWithSource(source, message));
            }
        });
    }

    private <E extends JComponent> E waitForInput(final Callable<E> func) throws UndoClickedException {
        final AtomicReference<E> ref = new AtomicReference<>();
        final AtomicReference<Exception> except = new AtomicReference<>();
        invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    final E content = func.call();
                    ref.set(content);
                    userActionPanel.setContentPanel(content);
                } catch (Exception ex) {
                    except.set(ex);
                }
            }
        });
        waitForInput();
        if (except.get() != null) {
            throw new RuntimeException(except.get());
        } else {
            return ref.get();
        }
    }

    private Object[] getArtificialNextEventChoiceResults(final MagicEvent event) {
        disableActionButton(true);
        if (CONFIG.getHideAiActionPrompt()) {
            showMessage(MagicEvent.NO_SOURCE, "");
        } else {
            showMessage(event.getSource(),event.getChoiceDescription());
        }
        SwingGameController.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                //do nothing, ensure that event dispatch queue is cleared
            }
        });

        //dynamically get the AI based on the player's index
        final MagicPlayer player = event.getPlayer();
        final MagicAI ai = player.getAiProfile().getAiType().getAI();
        return ai.findNextEventChoiceResults(game, player);
    }

    private Object[] getPlayerNextEventChoiceResults(final MagicEvent event) throws UndoClickedException {
        final MagicSource source=event.getSource();
        setSourceCardDefinition(source);
        final Object[] choiceResults;
        try {
            choiceResults = event.getChoice().getPlayerChoiceResults(this,game,event.getPlayer(),source);
        } finally {
            clearValidChoices();
            setSourceCardDefinition(MagicEvent.NO_SOURCE);
        }
        return choiceResults;
    }

    private void executeNextEventWithChoices(final MagicEvent event) {
        final Object[] choiceResults;
        if (selfMode || event.getPlayer().isArtificial()) {
            choiceResults = getArtificialNextEventChoiceResults(event);
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
        if (!selfMode && waitForInputOrUndo()) {
            performUndo();
            updateGameView();
        } else {
            game.advanceDuel();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        gamePanel.close();
                    } catch (InvalidDeckException ex) {
                        ScreenController.showWarningMessage(ex.getMessage());
                    }
                }
            });
            running.set(false);
        }
    }

    /**
     * Capture the event and action associated with an AI player
     * playing a new card from their library.
     */
    private void setAnimationEvent(final MagicEvent event) {
        if (event.getPlayer().isArtificial() || MagicSystem.isAiVersusAi()) {
            final MagicEventAction action = event.getMagicEventAction();
            // action appears to be an instance of an anonymous inner class so "instanceof" does not work.
            // (see http://stackoverflow.com/questions/17048900/reflection-class-forname-finds-classes-classname1-and-classname2-what-a)
            final boolean isValidAction = action.getClass().getName().startsWith("magic.model.event.MagicCardActivation");
            if (event.isValid() && isValidAction) {
                gamePanel.setAnimationEvent(event);
            }
        }
    }

    private void executeNextEventOrPhase() {
        if (game.hasNextEvent()) {
            setAnimationEvent(game.getEvents().peek());
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
        if (!MagicSystem.isAiVersusAi() && !MagicSystem.isDebugMode()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    gamePanel.showEndGameMessage();
                }
            });
        }
        showMessage(MagicEvent.NO_SOURCE,
                "{L} " +
                game.getLosingPlayer() + " " +
                (gameConceded.get() ? "conceded" : "lost" ) +
                " the game.");
    }

    private void playEndGameSoundEffect() {
        if (game.getLosingPlayer().getIndex() == 0) {
            SoundEffects.playGameSound(game, SoundEffects.LOSE_SOUND);
        } else {
            SoundEffects.playGameSound(game, SoundEffects.WIN_SOUND);
        }
    }

    @Override
    public void messageLogged(final MagicLogBookEvent ev) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (ev.getMagicMessage() == null) {
                    gamePanel.getLogBookViewer().update();
                } else {
                    gamePanel.getLogBookViewer().addMagicMessage(ev.getMagicMessage());
                }
            }
        });
    }

    public void showChoiceCardPopup() {
        final MagicCardDefinition cardDefinition = getSourceCardDefinition();
        if (cardDefinition != MagicCardDefinition.UNKNOWN && !GeneralConfig.getInstance().getTextView()) {
            final Point point = userActionPanel.getLocationOnScreen();
            viewInfoRight(cardDefinition, 0, new Rectangle(point.x, point.y-20, userActionPanel.getWidth(), userActionPanel.getHeight()));
        }
    }

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

    private boolean isValidSaveState() {
        final boolean isHumanTurn = game.getTurnPlayer().isHuman();
        final boolean isHumanPriority = game.getPriorityPlayer().isHuman();
        final boolean isStackEmpty = game.getStack().isEmpty();
        return isHumanTurn && isHumanPriority && isFirstMainPhase() && isStackEmpty;
    }

    private boolean isFirstMainPhase() {
        if (game.getPhase() instanceof MagicMainPhase) {
            final MagicMainPhase phase = (MagicMainPhase)game.getPhase();
            return phase == MagicMainPhase.getFirstInstance();
        } else {
            return false;
        }
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
                        "You may pay the buyback " + costText + '.'
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
    public int getMultiKickerCountChoice(
            final MagicSource source,
            final MagicManaCost cost,
            final int maximumCount,
            final String name) throws UndoClickedException {
        final MultiKickerChoicePanel kickerPanel = waitForInput(new Callable<MultiKickerChoicePanel>() {
            @Override
            public MultiKickerChoicePanel call() {
                return new MultiKickerChoicePanel(SwingGameController.this, source, cost, maximumCount, name);
            }
        });
        return kickerPanel.getKicker();
    }

    @Override
    public int getSingleKickerCountChoice(
            final MagicSource source,
            final MagicManaCost cost,
            final String name) throws UndoClickedException {
        final MayChoicePanel kickerPanel = waitForInput(new Callable<MayChoicePanel>() {
            @Override
            public MayChoicePanel call() {
                return new MayChoicePanel(SwingGameController.this, source, "You may pay the " + name + ' ' + cost.getText() + '.');
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
    public boolean getTakeMulliganChoice(
            final MagicSource source,
            final MagicPlayer player) throws UndoClickedException {
        final MayChoicePanel choicePanel = waitForInput(new Callable<MayChoicePanel>() {
            @Override
            public MayChoicePanel call() {
                final boolean showMulliganScreen =
                        MulliganScreen.isActive() ||
                        (player.getHandSize() == DuelConfig.getInstance().getHandSize() &&
                         GeneralConfig.getInstance().showMulliganScreen());
                if (showMulliganScreen) {
                    return new MulliganChoicePanel(SwingGameController.this, source, "You may take a mulligan.", player.getPrivateHand());
                } else {
                    return new MayChoicePanel(SwingGameController.this, source, "You may take a mulligan.");
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

    @Override
    public Rectangle getPlayerZoneButtonRectangle(MagicPlayer player, MagicPlayerZone zone, Component canvas) {
        return gamePanel.getPlayerZoneButtonRectangle(player, zone, canvas);
    }

    @Override
    public Rectangle getStackViewerRectangle(Component canvas) {
        return gamePanel.getStackViewerRectangle(canvas);
    }

    public void doFlashPlayerHandZoneButton() {
        gamePanel.doFlashPlayerHandZoneButton();
    }
}
