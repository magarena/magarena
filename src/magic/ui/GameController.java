package magic.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import magic.ai.ArtificialWorkerPool;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.choice.MagicChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPriorityEvent;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.ChoiceViewer;
import magic.ui.viewer.GameViewer;

public class GameController {

	private static final long MAX_TEST_MODE_DURATION=10000;
	private static final int CARD_VIEW_DELAY=300;
	
	private final GamePanel gamePanel;
	private final MagicGame game;
	private final boolean testMode;
	private final AtomicBoolean running;
	private final Collection<ChoiceViewer> choiceViewers;
	private Set<Object> validChoices;
	private CardViewer cardViewer;
	private CardViewer imageCardViewer;
	private GameViewer gameViewer;
	private boolean gameConceded=false;
	private boolean undoClicked=false;
	private boolean actionClicked=false;
	private boolean combatChoice=false;
	private Object choiceClicked=null;
	private MagicCardDefinition sourceCardDefinition;
	
	public GameController(final GamePanel gamePanel,final MagicGame game) {
		
		this.gamePanel=gamePanel;
		this.game=game;
		testMode=(gamePanel==null);
		running=new AtomicBoolean(false);
		choiceViewers=new ArrayList<ChoiceViewer>();
		clearValidChoices();
	}
	
	/** Fully artificial test game. */
	public GameController(final MagicGame game) {
		
		this(null,game);
	}
		
	public void enableForwardButton() {
		
		gameViewer.enableButton(IconImages.FORWARD);
	}
	
	public void disableActionButton(final boolean thinking) {
		
		gameViewer.disableButton(thinking);
	}
	
	/** Returns true when undo was clicked. */
	public synchronized boolean waitForInputOrUndo() {

		try {
			wait();
			if (undoClicked) {
				undoClicked=false;
				return true;
			}
			return false;
		} catch (final InterruptedException ex) {
			return false;
		}
	}
	
	public synchronized void actionClicked() {

		undoClicked=false;
		actionClicked=true;
		choiceClicked=null;
		notifyAll();
	}
	
	public synchronized void undoClicked() {
		
		if (game.hasUndoPoints()) {
			undoClicked=true;
			actionClicked=false;
			choiceClicked=null;
			setSourceCardDefinition(null);
			clearValidChoices();
			notifyAll();
		}
	}

	public synchronized void processClick(final Object choice) {

		if (validChoices.contains(choice)) {
			undoClicked=false;
			actionClicked=false;
			choiceClicked=choice;
			notifyAll();
		}
	}
	
	public boolean isActionClicked() {
		
		return actionClicked;
	}
	
	public Object getChoiceClicked() {
		
		return choiceClicked;
	}
	
	public void setCardViewer(final CardViewer cardViewer) {
		
		this.cardViewer=cardViewer;
	}
	
	public void setImageCardViewer(final CardViewer cardViewer) {
		
		this.imageCardViewer=cardViewer;
	}
	
	public void setGameViewer(final GameViewer gameViewer) {
		
		this.gameViewer=gameViewer;
	}
	
	public void viewCard(final MagicCardDefinition cardDefinition) {
		
		cardViewer.setCard(cardDefinition);
	}
	
	public void viewInfoAbove(final MagicCardDefinition cardDefinition,final Rectangle rect) {

		final Dimension size=gamePanel.getSize();
		final Point pointOnScreen=gamePanel.getLocationOnScreen();
		rect.x-=pointOnScreen.x;
		rect.y-=pointOnScreen.y;
		int x=rect.x+(rect.width-imageCardViewer.getWidth())/2;
		int y=rect.y-imageCardViewer.getHeight()-6;
		if (x+imageCardViewer.getWidth()>=size.width) {	
			x=rect.x+rect.width-imageCardViewer.getWidth();
		}
		if (y<10) {
			y=rect.y+rect.height+6;
		}
		imageCardViewer.setCard(cardDefinition);
		imageCardViewer.setLocation(x,y);
		DelayedViewersThread.getInstance().showViewer(imageCardViewer,CARD_VIEW_DELAY);
	}
	
	public void viewInfoRight(final MagicCardDefinition cardDefinition,final Rectangle rect) {

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
		imageCardViewer.setCard(cardDefinition);
		imageCardViewer.setLocation(x,y);		
		DelayedViewersThread.getInstance().showViewer(imageCardViewer,CARD_VIEW_DELAY);
	}
	
	public void hideInfo() {
		
		DelayedViewersThread.getInstance().hideViewer(imageCardViewer);
	}

	public void setSourceCardDefinition(final MagicSource source) {
		
		if (source!=null) {
			sourceCardDefinition=source.getCardDefinition();
		} else {
			sourceCardDefinition=null;
		}
	}
	
	public MagicCardDefinition getSourceCardDefinition() {
		
		return sourceCardDefinition;
	}
	
	public void focusViewers(final int handGraveyard,final int stackCombat) {

		gamePanel.focusViewers(handGraveyard,stackCombat);
	}
	
	public void registerChoiceViewer(final ChoiceViewer choiceViewer) {
		
		choiceViewers.add(choiceViewer);
	}
	
	private void showValidChoices() {
		
		SwingUtilities.invokeLater(new Runnable() {

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
	
	public void setValidChoices(final Set<Object> validChoices,final boolean combatChoice) {
		
		this.validChoices=validChoices;
		this.combatChoice=combatChoice;
		showValidChoices();
	}
	
	public Set<Object> getValidChoices() {
		
		return validChoices;
	}

	public void update() {
		
		hideInfo();
		gamePanel.updateInfo();
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				gamePanel.update();
			}
		});
	}
	
	public String getMessageWithSource(final MagicSource source,final String message) {

		if (source==null) {
			return message;
		} else {
			return "("+source+")|"+message;
		}
	}
		
	public void showMessage(final MagicSource source,final String message) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				gameViewer.showMessage(getMessageWithSource(source,message));
			}
		});
	}
	
	public void showComponent(final JComponent content) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				gameViewer.showComponent(content);
			}
		});
	}

	private Object[] getArtificialNextEventChoiceResults(final MagicEvent event) {

		if (!testMode) {
			disableActionButton(true);
			showMessage(event.getSource(),event.getChoiceDescription());
		}
		final ArtificialWorkerPool aiThreadPool=new ArtificialWorkerPool(game,event.getPlayer());
		final Object choiceResults[]=aiThreadPool.findNextEventChoiceResults();
		return choiceResults;
	}

	private Object[] getPlayerNextEventChoiceResults(final MagicEvent event) {

		final MagicSource source=event.getSource();
		setSourceCardDefinition(source);
		final Object choiceResults[]=event.getChoice().getPlayerChoiceResults(this,game,event.getPlayer(),source);
		clearValidChoices();
		setSourceCardDefinition(null);
		return choiceResults;
	}

	private void executeNextEventWithChoices(final MagicEvent event) {

		final Object[] choiceResults;
		if (testMode||event.getPlayer().getPlayerDefinition().isArtificial()) {
			choiceResults=getArtificialNextEventChoiceResults(event);
		} else {
			choiceResults=getPlayerNextEventChoiceResults(event);
			if (gameConceded) {
				return;
			}
			if (choiceResults==MagicChoice.UNDO_CHOICE_RESULTS) {
				game.gotoLastUndoPoint();
				return;
			}
		}
		game.executeNextEvent(choiceResults);
	}
	
	public synchronized void concede() {

		if (!gameConceded&&!game.isFinished()) {
			game.setLosingPlayer(game.getPlayer(0));
			game.clearUndoPoints();
			gameConceded=true;
			undoClicked=true;
			notifyAll();
		}
	}

	public void haltGame() {
		
		running.set(false);
	}
	
	public void runGame() {

		final long startTime=System.currentTimeMillis();
		
		running.set(true);
		while (running.get()) {
			
			if (game.isFinished()) {
				if (testMode) {
					game.advanceTournament();
					return;
				}
							
				game.logMessages();
				clearValidChoices();
				showMessage(null,"{L} "+game.getLosingPlayer()+" "+(gameConceded?"conceded":"lost")+" the game.|Press {f} to continue.");
				enableForwardButton();
				if (waitForInputOrUndo()) {
					game.gotoLastUndoPoint();
					update();
					continue;
				} else {
					game.advanceTournament();
					gamePanel.close();
					return;
				}
			}
			
			if (game.hasNextEvent()) {
				final MagicEvent event=game.getNextEvent();
				if (event instanceof MagicPriorityEvent) {
					game.logMessages();
				}
				if (event.hasChoice()) {
					executeNextEventWithChoices(event);
				} else {
					game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
				}
			} else {
				game.getPhase().executePhase(game);
			}

			if (testMode) {
				if (System.currentTimeMillis()-startTime>MAX_TEST_MODE_DURATION) {
					break;
				}
			} else {
				update();				
			}
		}
		running.set(false);
	}	
}