package magic.ui;

import magic.ui.duel.DuelPanel;
import magic.MagicUtility;
import magic.ai.MagicAI;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.data.SoundEffects;
import magic.model.ILogBookListener;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLogBookEvent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.phase.MagicPhaseType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPriorityEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.duel.viewer.ChoiceViewer;
import magic.ui.duel.viewer.UserActionPanel;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import magic.ui.duel.viewer.ViewerInfo;

public class GameController implements ILogBookListener {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private long MAX_TEST_MODE_DURATION=10000;

    private final DuelPanel gamePanel;
    private final MagicGame game;
    // isDeckStrMode is true when game is run via DeckStrengthViewer or DeckStrCal.
    private final boolean isDeckStrMode;
    private final boolean selfMode = Boolean.getBoolean("selfMode");
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean gameConceded = new AtomicBoolean(false);
    private final Collection<ChoiceViewer> choiceViewers = new ArrayList<>();
    private Set<?> validChoices;
    private CardViewer imageCardViewer;
    private UserActionPanel userActionPanel;
    private boolean actionClicked;
    private boolean combatChoice;
    private boolean resetGame;
    private MagicTarget choiceClicked = MagicTargetNone.getInstance();
    private MagicCardDefinition sourceCardDefinition = MagicCardDefinition.UNKNOWN;
    private final BlockingQueue<Boolean> input = new SynchronousQueue<>();
    private int gameTurn = 0;
    private final ViewerInfo viewerInfo;

    public GameController(final DuelPanel aGamePanel,final MagicGame aGame) {
        gamePanel = aGamePanel;
        game = aGame;
        isDeckStrMode = false;
        clearValidChoices();
        if (!CONFIG.isLogViewerDisabled()) {
            game.getLogBook().addListener(this);
        }
        viewerInfo = new ViewerInfo(game);
    }

    /** Fully artificial test game. */
    public GameController(final MagicGame aGame) {
        gamePanel = null;
        game = aGame;
        isDeckStrMode = true;
        clearValidChoices();
        viewerInfo = null;
    }

    public MagicGame getGame() {
        return game;
    }

    public void enableForwardButton() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userActionPanel.enableButton(IconImages.FORWARD);
            }
        });
    }

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

    public void waitForInput() throws UndoClickedException {
        try {
            final boolean undoClicked = input.take();
            if (undoClicked) {
                throw new UndoClickedException();
            }
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void resume(final boolean undoClicked) {
        input.offer(undoClicked);
    }
    
    public void switchKeyPressed() {
        game.setVisiblePlayer(game.getVisiblePlayer().getOpponent());
        getViewerInfo().update(game);
        gamePanel.updateView();
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

    public boolean isActionClicked() {
        return actionClicked;
    }

    @SuppressWarnings("unchecked")
    public <T> T getChoiceClicked() {
        return (T)choiceClicked;
    }

    public void setMaxTestGameDuration(final long duration) {
        MAX_TEST_MODE_DURATION = duration;
    }

    public void setImageCardViewer(final CardViewer cardViewer) {
        this.imageCardViewer=cardViewer;
    }

    public void setUserActionPanel(final UserActionPanel userActionPanel) {
        this.userActionPanel = userActionPanel;
    }

    public void viewInfoAbove(final MagicCardDefinition cardDefinition,final int index,final Rectangle rect) {
        final Dimension size=gamePanel.getSize();
        final Point pointOnScreen=gamePanel.getLocationOnScreen();
        rect.x-=pointOnScreen.x;
        rect.y-=pointOnScreen.y;
        final int imageWidth=imageCardViewer.getWidth();
        final int imageHeight=imageCardViewer.getHeight();
        int x=rect.x+(rect.width-imageWidth)/2;
        final int y1=rect.y-imageHeight-6;
        final int y2=rect.y+rect.height+6;
        final int dy2=size.height-y2-imageHeight;
        if (x+imageWidth>=size.width) {
            x=rect.x+rect.width-imageWidth;
        }
        int y;
        // Position is next to card?
        if (y1<10&&dy2<10) {
            x=rect.x-6-imageWidth;
            if (x < 0) {
                x = rect.x + rect.width + 6;
            }
            if (y1>=dy2) {
                y=rect.y+rect.height-imageHeight;
                if (y<10) {
                    y=10;
                }
            } else {
                y=rect.y;
                if (y+imageHeight+10>size.height) {
                    y=size.height-10-imageHeight;
                }
            }
        // Position is above card?
        } else if (y1>=10) {
            y=y1;
        // Position if beneath card.
        } else {
            y=y2;
        }
        imageCardViewer.setCard(cardDefinition, index);
        imageCardViewer.setLocation(x,y);
        imageCardViewer.showDelayed(getPopupDelay());
    }

    public void viewInfoRight(final MagicCardDefinition cardDefinition,final int index,final Rectangle rect) {
        final Dimension size=gamePanel.getSize();
        final Point pointOnScreen=gamePanel.getLocationOnScreen();
        rect.x-=pointOnScreen.x;
        rect.y-=pointOnScreen.y;
        final int x=rect.x+rect.width+10;
        final int maxY=size.height-8-imageCardViewer.getHeight();
        int y=rect.y+(rect.height-imageCardViewer.getHeight())/2;
        if (y<10) {
            y=10;
        } else if (y>maxY) {
            y=maxY;
        }
        imageCardViewer.setCard(cardDefinition, index);
        imageCardViewer.setLocation(x,y);
        imageCardViewer.showDelayed(getPopupDelay());
    }

    private int getPopupDelay() {
        return CONFIG.isMouseWheelPopup() ? 0 : CONFIG.getPopupDelay();
    }

    public void hideInfo() {
        imageCardViewer.hideDelayed();
    }

    public void setSourceCardDefinition(final MagicSource source) {
        sourceCardDefinition=source.getCardDefinition();
    }

    public MagicCardDefinition getSourceCardDefinition() {
        return sourceCardDefinition;
    }

    public void focusViewers(final int handGraveyard) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gamePanel.focusViewers(handGraveyard);
            }
        });
    }

    public void clearCards() {
        showCards(new MagicCardList());
    }

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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (final ChoiceViewer choiceViewer : choiceViewers) {
                    choiceViewer.showValidChoices(validChoices);
                }
            }
        });
    }

    public boolean isCombatChoice() {
        return combatChoice;
    }

    public void clearValidChoices() {
        validChoices=Collections.emptySet();
        combatChoice=false;
        showValidChoices();
    }

    public void setValidChoices(final Set<?> aValidChoices,final boolean aCombatChoice) {
        this.validChoices=aValidChoices;
        this.combatChoice=aCombatChoice;
        showValidChoices();
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

    public void showMessage(final MagicSource source,final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userActionPanel.showMessage(getMessageWithSource(source,message));
            }
        });
    }

    public <E extends JComponent> E waitForInput(final Callable<E> func) throws UndoClickedException {
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
        if (!isDeckStrMode) {
            disableActionButton(true);
            showMessage(event.getSource(),event.getChoiceDescription());
            GameController.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    //do nothing, ensure that event dispatch queue is cleared
                }
            });
        }

        //dynamically get the AI based on the player's index
        final MagicPlayer player = event.getPlayer();
        final MagicAI ai = game.getDuel().getAIs()[player.getIndex()];
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
        if (selfMode || isDeckStrMode || event.getPlayer().getPlayerDefinition().isArtificial()) {
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

    public void haltGame() {
        running.set(false);
    }

    /**
     * Main game loop runs on separate thread.
     */
    public void runGame() {
        final long startTime=System.currentTimeMillis();
        running.set(true);
        while (running.get()) {
            if (game.isFinished()) {
                doNextActionOnGameFinished();
            } else {
                executeNextEventOrPhase();
                if (isDeckStrMode) {
                    if (System.currentTimeMillis() - startTime > MAX_TEST_MODE_DURATION) {
                        System.err.println("WARNING. Max time for AI game exceeded");
                        running.set(false);
                    }
                } else {
                    updateGameView();
                }
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
        if (isDeckStrMode) {
            game.advanceDuel();
            running.set(false);
        } else {
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
                        gamePanel.close();
                    }
                });
                running.set(false);
            }
        }
    }

    /**
     * Capture the event and action associated with an AI player
     * playing a new card from their library.
     */
    private void setAnimationEvent(final MagicEvent event) {
        if (event.getPlayer().getPlayerDefinition().isArtificial() || MagicUtility.isAiVersusAi()) {
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
            if (gamePanel != null) {
                setAnimationEvent(game.getEvents().peek());
            }
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
        if (!MagicUtility.isAiVersusAi() && !MagicUtility.isDebugMode()) {
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
            SoundEffects.playClip(SoundEffects.LOSE_SOUND);
        } else {
            SoundEffects.playClip(SoundEffects.WIN_SOUND);
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
}
