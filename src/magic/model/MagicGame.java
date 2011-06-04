package magic.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.data.GeneralConfig;
import magic.model.action.MagicAction;
import magic.model.action.MagicActionList;
import magic.model.action.MagicAddEventAction;
import magic.model.action.MagicExecuteFirstEventAction;
import magic.model.action.MagicLogMarkerAction;
import magic.model.action.MagicLoseGameAction;
import magic.model.action.MagicMarkerAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicCombatCreature;
import magic.model.choice.MagicDeclareAttackersResult;
import magic.model.choice.MagicDeclareBlockersResult;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventQueue;
import magic.model.phase.MagicGameplay;
import magic.model.phase.MagicPhase;
import magic.model.phase.MagicPhaseType;
import magic.model.phase.MagicStep;
import magic.model.stack.MagicItemOnStack;
import magic.model.stack.MagicStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetNone;
import magic.model.target.MagicTargetType;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicPermanentTriggerList;
import magic.model.trigger.MagicPermanentTriggerMap;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class MagicGame {

	public static final boolean LOSE_DRAW_EMPTY_LIBRARY=true;
	public static final int LOSING_POISON=10;
	
	private static final long ID_FACTOR=13;
	
	private final MagicTournament tournament;
	private final MagicPlayer players[];
	private final MagicPermanentTriggerMap triggers;
	private final MagicPermanentTriggerList turnTriggers;
	private final MagicCardList exiledUntilEndOfTurn;
	private final MagicEventQueue events;
	private final MagicStack stack;
	private final MagicPlayer scorePlayer;
	private final boolean sound;
	private long identifiers[];
	private int score=0;
	private int turn=1;
	private int startTurn=0;
	private int mainPhaseCount=100000000;
	private boolean landPlayed=false;
	private boolean priorityPassed=false;
	private int priorityPassedCount=0;
    private boolean passPriority=false;
	private boolean stateCheckRequired=false;
	private boolean artificial;
	private boolean fastChoices=false;
	private boolean immediate=false;
	private MagicPlayer visiblePlayer;
	private MagicPlayer turnPlayer;
	private MagicPlayer losingPlayer=null;
	private MagicGameplay gameplay;
	private MagicPhase phase;
	private MagicStep step;
	private MagicPayedCost payedCost;
	private MagicActionList actions;
	private MagicActionList undoPoints;
	private final MagicLogBook logBook;
	private final MagicLogMessageBuilder logMessageBuilder;
	
	public MagicGame(final MagicTournament tournament,final MagicGameplay gameplay,final MagicPlayer players[],final MagicPlayer startPlayer,final boolean sound) {

		artificial=false;
		this.tournament=tournament;
		this.gameplay=gameplay;
		this.players=players;
		this.sound=sound;
		identifiers=new long[MagicIdentifierType.NR_OF_IDENTIFIERS];
		triggers=new MagicPermanentTriggerMap();
		turnTriggers=new MagicPermanentTriggerList();
		exiledUntilEndOfTurn=new MagicCardList();
		events=new MagicEventQueue();
		stack=new MagicStack();
		visiblePlayer=players[0];
		scorePlayer=visiblePlayer;
		turnPlayer=startPlayer;
		actions=new MagicActionList();
		undoPoints=new MagicActionList();
		logBook=new MagicLogBook();
		logMessageBuilder=new MagicLogMessageBuilder(this);
		payedCost=new MagicPayedCost();
		changePhase(gameplay.getStartPhase(this));
	}
	
	public MagicGame(final MagicGame game,final MagicPlayer scorePlayer) {
		
		artificial=true;
		sound=false;
		final MagicCopyMap copyMap=new MagicCopyMap();		
		this.tournament=game.tournament;
		this.gameplay=game.gameplay;
		this.players=copyMap.copyObjects(game.players,MagicPlayer.class);		
		this.identifiers=Arrays.copyOf(game.identifiers,MagicIdentifierType.NR_OF_IDENTIFIERS);
		this.triggers=new MagicPermanentTriggerMap(copyMap,game.triggers);
		this.turnTriggers=new MagicPermanentTriggerList(triggers,game.turnTriggers);
		this.exiledUntilEndOfTurn=game.exiledUntilEndOfTurn.copy(copyMap);
		this.events=new MagicEventQueue(copyMap,game.events);
		this.stack=new MagicStack(copyMap,game.stack);
		this.scorePlayer=copyMap.copy(scorePlayer);
		this.score=0;
		this.turn=game.turn;
		this.startTurn=game.startTurn;
		this.landPlayed=game.landPlayed;
		this.priorityPassed=game.priorityPassed;
		this.stateCheckRequired=game.stateCheckRequired;
		this.visiblePlayer=copyMap.copy(game.visiblePlayer);
		this.turnPlayer=copyMap.copy(game.turnPlayer);
		this.phase=game.phase;
		this.step=game.step;
		this.payedCost=new MagicPayedCost(copyMap,game.payedCost);
		this.actions=new MagicActionList();
		this.undoPoints=null;
		this.logBook=null;
		this.logMessageBuilder=null;
	}
	
    public void setPassPriority(final boolean pp) {
        passPriority = pp;
    }

    public boolean getPassPriority() {
        return passPriority;
    }

	public void setScore(final int score) {
		
		this.score=score;
	}
	
	public void changeScore(final int amount) {
		
		score+=amount;
	}
	
	public int getScore() {
		
		return score;
	}
	
	public long getGameId(final int pruneScore) {
		
		long id=(turn*ID_FACTOR+phase.getType().getIndex())*ID_FACTOR+score+pruneScore;
		id=players[0].getPlayerId(id);
		id=players[1].getPlayerId(id);
		return id;
	}
	
	public boolean canSkipSingleChoice() {
		if (GeneralConfig.getInstance().getSkipSingle()) {
			if (phase.getType()==MagicPhaseType.DeclareBlockers) {
				return (turnPlayer!=visiblePlayer||turnPlayer.getNrOfAttackers()==0); //&&stack.isEmpty();
			}
			return true; //stack.isEmpty();
		}
		return false;
	}
	
	public boolean canSkipSingleManaChoice() {
		return GeneralConfig.getInstance().getSkipSingle();
	}
	
	public boolean canSkipDeclareBlockersSingleChoice() {
		return GeneralConfig.getInstance().getSkipSingle()&&turnPlayer.getNrOfAttackers()==0;
	}		
	
	public boolean canAlwaysPass() {
		
		if (GeneralConfig.getInstance().getAlwaysPass()) {
			return phase.getType()==MagicPhaseType.Draw||phase.getType()==MagicPhaseType.BeginOfCombat;
		}
		return false;
	}
	
	public int getArtificialLevel() {
		
		return tournament.getDifficulty();
	}

	public boolean isArtificial() {
		
		return artificial;
	}
	
	public boolean isSound() {
		
		return sound;
	}
	
	public void setFastChoices(final boolean fastChoices) {
		
		this.fastChoices=fastChoices;
	}
	
	public boolean getFastChoices() {
		
		return fastChoices;
	}
			
	public void setTurn(final int turn) {
		
		this.turn=turn;
	}
	
	public int getTurn() {
		
		return turn;
	}
	
	public void setMainPhases(final int count) {

		startTurn=turn;
		mainPhaseCount=count;
	}
	
	public int getRelativeTurn() {
	
		return startTurn>0?turn-startTurn:0;
	}
	
	public void decreaseMainPhaseCount() {
		
		mainPhaseCount--;
	}
	
	public void setMainPhaseCount(final int count) {
		
		mainPhaseCount=count;
	}
	
	public int getMainPhaseCount() {
		
		return mainPhaseCount;
	}
	
	public MagicGameplay getGameplay() {
		
		return gameplay;
	}
	
	public void setPhase(final MagicPhase phase) {
		this.phase=phase;
	}
	
	public void changePhase(final MagicPhase phase) {
        if (phase.getType() == MagicPhaseType.Untap && turnPlayer.getIndex() == 0) {
            passPriority=false;
        }
		this.phase=phase;
		step=MagicStep.Begin;
		priorityPassedCount=0;
		players[0].getActivationPriority().clear();
		players[1].getActivationPriority().clear();
	}
	
	public MagicPhase getPhase() {
		
		return phase;
	}
	
	public boolean isPhase(final MagicPhaseType type) {
		
		return phase.getType()==type;
	}
	
	public boolean isMainPhase() {
		
		return phase.getType().isMain();
	}
	
	public void setStep(final MagicStep step) {
		
		this.step=step;
	}
		
	public MagicStep getStep() {
		
		return step;
	}
			
	public void resolve() {

		if (stack.isEmpty()) {
			step=MagicStep.NextPhase;
		} else {
			step=MagicStep.Resolve;
		}		
	}
		
	public MagicPayedCost getPayedCost() {
		
		return payedCost;
	}
	
	/** Determines if game score should be cached for this game state. */
	public boolean cacheState() {
		
		switch (phase.getType()) {
			case FirstMain:
			case EndOfCombat:
			case Cleanup:
				return step==MagicStep.NextPhase;
			default:
				return false;
		}
	}

	/** Tells gameplay that is can skip certain parts during AI processing. */
	public boolean canSkip() {
		
		return stack.isEmpty()&&artificial;
	}
	
	public boolean isFinished() {
		
		return losingPlayer!=null||mainPhaseCount<=0;
	}

	public MagicLogBook getLogBook() {
		
		return logBook;
	}
		
	public void setKnownCards() {

		getOpponent(scorePlayer).setHandToUnknown();
		for (final MagicPlayer player : players) {
			
			player.getLibrary().setKnown(false);
		}		
	}

	public long createIdentifier(final MagicIdentifierType type) {

		return identifiers[type.getIndex()]++;
	}
	
	public void releaseIdentifier(final MagicIdentifierType type) {
		
		identifiers[type.getIndex()]--;
	}
	
	public void setIdentifiers(final long identifiers[]) {
		
		this.identifiers=identifiers;
	}
	
	public long[] getIdentifiers() {
		
		return Arrays.copyOf(identifiers,identifiers.length);
	}
		
	public void startActions() {
		
		doAction(new MagicMarkerAction());
	}
		
	public void doAction(final MagicAction action) {

		actions.add(action);
		action.doAction(this);
		score+=action.getScore(scorePlayer);
	}
		
	public void undoActions() {
		
		while (true) {

			final MagicAction action=actions.removeLast();
			action.undoAction(this);
			if (action instanceof MagicMarkerAction) {
				break;
			}
		}
	}
	
	public void undoAllActions() {

		while (!actions.isEmpty()) {

			final MagicAction action=actions.removeLast();
			action.undoAction(this);
		}
	}
	
	public Collection<MagicAction> getActions() {
		
		return actions;
	}
	
	public void createUndoPoint() {
		
		final MagicAction markerAction=new MagicMarkerAction();
		doAction(markerAction);
		doAction(new MagicLogMarkerAction());
		undoPoints.addLast(markerAction);
	}
		
	public void gotoLastUndoPoint() {

		final MagicAction markerAction=undoPoints.removeLast();
		while (true) {

			final MagicAction action=actions.removeLast();
			action.undoAction(this);
			if (action==markerAction) {
				break;
			}
		}
	}
			
	public int getNrOfUndoPoints() {
		
		return undoPoints.size();
	}
	
	public boolean hasUndoPoints() {
		
		return !undoPoints.isEmpty();
	}
	
	public void clearUndoPoints() {
		
		undoPoints.clear();
	}
	
	public void clearMessages() {
		
		logMessageBuilder.clearMessages();
	}
	
	public void logMessages() {
		
		if (logMessageBuilder!=null) {
			logMessageBuilder.logMessages();
		}
	}

	public void logAppendEvent(final MagicEvent event,final Object choiceResults[]) {
		
		if (logMessageBuilder!=null) {
			final String message=event.getDescription(choiceResults);
			if (message!=null) {
				logMessageBuilder.appendMessage(event.getPlayer(),message);
			}
		}
	}
	
	public void logAppendMessage(final MagicPlayer player,final String message) {
		
		if (logMessageBuilder!=null) {
			logMessageBuilder.appendMessage(player,message);
		}
	}
	
	public void logMessage(final MagicPlayer player,final String message) {
		
		if (logBook!=null) {
			logBook.add(new MagicMessage(this,player,message));
		}
	}
	
	public void logAttackers(final MagicPlayer player,final MagicDeclareAttackersResult result) {
		
		if (logBook!=null&&!result.isEmpty()) {
			final SortedSet<String> names=new TreeSet<String>();
			for (final MagicPermanent attacker : result) {
				
				names.add(attacker.getName());
			}
			final StringBuilder builder=new StringBuilder("You attack with ");
			MagicMessage.addNames(builder,names);
			builder.append('.');
			logBook.add(new MagicMessage(this,player,builder.toString()));
		}
	}
	
	public void logBlockers(final MagicPlayer player,final MagicDeclareBlockersResult result) {

		if (logBook!=null) {
			final SortedSet<String> names=new TreeSet<String>();
			for (final MagicCombatCreature[] creatures : result) {
				
				for (int index=1;index<creatures.length;index++) {
					
					names.add(creatures[index].getName());
				}
			}
			if (!names.isEmpty()) {
				final StringBuilder builder=new StringBuilder("You block with ");
				MagicMessage.addNames(builder,names);
				builder.append('.');
				logBook.add(new MagicMessage(this,player,builder.toString()));
			}
		}
	}
	
	public void executeEvent(final MagicEvent event,final Object choiceResults[]) {
			
		if (choiceResults!=null) {
			logAppendEvent(event,choiceResults);
			// Payed cost.
			if (choiceResults.length==1) {
				payedCost.set(choiceResults[0]);
			}
			event.executeEvent(this,choiceResults);
			checkState();
		}
	}

	public MagicEventQueue getEvents() {
		
		return events;
	}
		
	public boolean hasNextEvent() {
		
		return !events.isEmpty();
	}
	
	public MagicEvent getNextEvent() {
		
		return events.getFirst();
	}
	
	public void addEvent(final MagicEvent event) {
		
		doAction(new MagicAddEventAction(event));
	}
		
	public void executeNextEvent(final Object choiceResults[]) {

		doAction(new MagicExecuteFirstEventAction(choiceResults));
	}
	
	public MagicTournament getTournament() {
		
		return tournament;
	}
	
	public void advanceTournament() {
		
		tournament.advance(losingPlayer!=players[0]);
	}
	
	public MagicPlayer[] getPlayers() {

		return players;
	}
	
	public MagicPlayer getPlayer(final int index) {

		return players[index];
	}
	
	public MagicPlayer getOpponent(final MagicPlayer player) {

		return players[1-player.getIndex()];		
	}
	
	public void setVisiblePlayer(final MagicPlayer visiblePlayer) {
		
		this.visiblePlayer=visiblePlayer;
	}
	
	public MagicPlayer getVisiblePlayer() {
		
		return visiblePlayer;
	}
		
	public void setTurnPlayer(final MagicPlayer turnPlayer) {
		
		this.turnPlayer=turnPlayer;
	}
		
	public MagicPlayer getTurnPlayer() {
		
		return turnPlayer;
	}
	
	public MagicPlayer getPriorityPlayer() {
		
		return step==MagicStep.ActivePlayer?turnPlayer:getOpponent(turnPlayer);
	}
		
	public MagicPlayer getScorePlayer() {
		
		return scorePlayer;
	}
	
	public void setLosingPlayer(final MagicPlayer player) {
		
		losingPlayer=player;
	}
	
	public MagicPlayer getLosingPlayer() {
		
		return losingPlayer;
	}
			
	public boolean hasTurn(final MagicPlayer player) {

		return player==turnPlayer;
	}
	
	public int getCount(final int cardDefinitionIndex) {
		
		return players[0].getCount(cardDefinitionIndex)+players[1].getCount(cardDefinitionIndex);
	}
	
	public int getOtherPlayerCount(final int cardDefinitionIndex,final MagicPlayer player) {
		
		if (players[0]!=player) {
			return players[0].getCount(cardDefinitionIndex);
		} else {
			return players[1].getCount(cardDefinitionIndex);
		}
	}
	
	public int getNrOfPermanents(final MagicType type) {
		
		return players[0].getNrOfPermanentsWithType(type)+players[1].getNrOfPermanentsWithType(type);
	}
			
	public boolean canPlaySorcery(final MagicPlayer controller) {

		return phase.getType().isMain()&&stack.isEmpty()&&turnPlayer==controller;
	}
	
	public boolean canPlayLand(final MagicPlayer controller) {
		
		return !landPlayed&&canPlaySorcery(controller);
	}

	public boolean isLandPlayed() {
		
		return landPlayed;
	}
	
	public void setLandPlayed(final boolean landPlayed) {

		this.landPlayed=landPlayed;
	}
			
	public MagicStack getStack() {
		
		return stack;
	}
	
	public void setPriorityPassed(final boolean passed) {
		
		priorityPassed=passed;
	}
	
	public boolean getPriorityPassed() {
		
		return priorityPassed;
	}
	
	public void incrementPriorityPassedCount() {
		
		priorityPassedCount++;
	}
	
	public void setPriorityPassedCount(final int count) {
		
		priorityPassedCount=count;
	}
	
	public int getPriorityPassedCount() {
		
		return priorityPassedCount;
	}
						
	public MagicPermanent createPermanent(final MagicCard card,final MagicPlayer controller) {
		
		return new MagicPermanent(createIdentifier(MagicIdentifierType.Permanent),card,controller);
	}
	
	public MagicCardList getExiledUntilEndOfTurn() {
		
		return exiledUntilEndOfTurn;
	}
	
	public void setStateCheckRequired(final boolean required) {
		
		stateCheckRequired=required;
	}
	
	public void setStateCheckRequired() {
		
		stateCheckRequired=true;
	}
	
	public boolean getStateCheckRequired() {
		
		return stateCheckRequired;
	}
	
	public void checkState() {
		
		if (!stateCheckRequired) {
			return;
		}

		// Check permanents.
		do {
			
			stateCheckRequired=false;
			for (final MagicPlayer player : players) {
				
				for (final MagicPermanent permanent : player.getPermanents()) {
					
					permanent.checkState(this);
					// Stop at first change because a permanent could be removed from play.
					if (stateCheckRequired) {
						break;
					}
				}
			}
		} while (stateCheckRequired);

		// Check if a player has lost.
		final MagicPlayer lowestLifePlayer=(players[1].getLosingLife()<=players[0].getLosingLife())?players[1]:players[0];
		if (lowestLifePlayer.getLosingLife()<=0) {
			doAction(new MagicLoseGameAction(lowestLifePlayer,MagicLoseGameAction.LIFE_REASON));			
		}
		final MagicPlayer highestPoisonPlayer=(players[1].getLosingPoison()>=players[0].getLosingPoison())?players[1]:players[0];
		if (highestPoisonPlayer.getLosingPoison()>=LOSING_POISON) {
			doAction(new MagicLoseGameAction(highestPoisonPlayer,MagicLoseGameAction.POISON_REASON));
		}
	}

	public void checkLegendRule(final MagicPermanent permanent) {
		
		final MagicCardDefinition cardDefinition=permanent.getCardDefinition();
		if (cardDefinition.hasType(MagicType.Legendary)&&getCount(cardDefinition.getIndex())>1) {
			final String message="Put "+cardDefinition.getName()+" into its owner's graveyard (legend rule).";
			final MagicTargetFilter targetFilter=new MagicTargetFilter.CardTargetFilter(cardDefinition);
			final Collection<MagicTarget> targets=filterTargets(permanent.getController(),targetFilter);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent targetPermanent=(MagicPermanent)target;
				logAppendMessage(targetPermanent.getController(),message);
				doAction(new MagicRemoveFromPlayAction(targetPermanent,MagicLocationType.Graveyard));
			}
		}
	}
		
	public Object[] map(final Object data[]) {

		final int length=data.length;
		final Object mappedData[]=new Object[length];
		for (int index=0;index<length;index++) {
			
			final Object obj=data[index];
			if (obj!=null&&obj instanceof MagicMappable) {
				mappedData[index]=((MagicMappable)obj).map(this);
			} else {
				mappedData[index]=obj;
			}
		}	
		return mappedData;
	}
	
	// ***** TARGETS *****
	
	public List<MagicTarget> filterTargets(final MagicPlayer player,final MagicTargetFilter targetFilter,final MagicTargetHint targetHint) {

		final List<MagicTarget> targets=new ArrayList<MagicTarget>();
		
		// Players
		if (targetFilter.acceptType(MagicTargetType.Player)) {
			for (final MagicPlayer targetPlayer : players) {
				
				if (targetFilter.accept(this,player,targetPlayer)&&targetHint.accept(player,targetPlayer)) {
					targets.add(targetPlayer);
				}				
			}
		}
		
		// Permanents
		if (targetFilter.acceptType(MagicTargetType.Permanent)) {
			for (final MagicPlayer controller : players) {
			
				for (final MagicPermanent targetPermanent : controller.getPermanents()) {
				
					if (targetFilter.accept(this,player,targetPermanent)&&targetHint.accept(player,targetPermanent)) {
						targets.add(targetPermanent);
					}
				}
			}
		}		

		// Items on stack
		if (targetFilter.acceptType(MagicTargetType.Stack)) {
			for (final MagicItemOnStack targetItemOnStack : stack) {
				
				if (targetFilter.accept(this,player,targetItemOnStack)&&targetHint.accept(player,targetItemOnStack)) {
					targets.add(targetItemOnStack);
				}
			}
		}
			
		// Cards in graveyard
		if (targetFilter.acceptType(MagicTargetType.Graveyard)) {
			for (final MagicCard targetCard : player.getGraveyard()) {
				
				if (targetFilter.accept(this,player,targetCard)) {
					targets.add(targetCard);
				}				
			}
		}

		// Cards in opponent's graveyard
		if (targetFilter.acceptType(MagicTargetType.OpponentsGraveyard)) {
			for (final MagicCard targetCard : getOpponent(player).getGraveyard()) {
				
				if (targetFilter.accept(this,player,targetCard)) {
					targets.add(targetCard);
				}				
			}
		}
		
		return targets;
	}
		
	public List<MagicTarget> filterTargets(final MagicPlayer player,final MagicTargetFilter targetFilter) {
		
		return filterTargets(player,targetFilter,MagicTargetHint.None);
	}

	public boolean hasLegalTargets(final MagicPlayer player,final MagicSource source,final MagicTargetChoice targetChoice,final boolean hints) {
	
		final Collection<MagicTarget> targets=filterTargets(player,targetChoice.getTargetFilter(),targetChoice.getTargetHint(hints));
		if (!targetChoice.isTargeted()) {
			return !targets.isEmpty();
		}
		for (final MagicTarget target : targets) {

			if (target.isValidTarget(this,source)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Object> getLegalTargets(final MagicPlayer player,final MagicSource source,
			final MagicTargetChoice targetChoice,final MagicTargetHint targetHint) {

		final Collection<MagicTarget> targets=filterTargets(player,targetChoice.getTargetFilter(),targetHint);
		final List<Object> options;
		if (targetChoice.isTargeted()) {
			options=new ArrayList<Object>();
			for (final MagicTarget target : targets) {
	
				if (target.isValidTarget(this,source)) {
					options.add(target);
				}
			}
		} else {
			options=new ArrayList<Object>(targets);
		}
		// Add none when there are no legal targets. Should be only the case with triggers.
		if (options.isEmpty()) {
			options.add(MagicTargetNone.getInstance());
		}
		return options;
	}
	
	public boolean filterTarget(final MagicPlayer player,final MagicTargetFilter targetFilter,final MagicTarget target) {
	
		if (target==null||target==MagicTargetNone.getInstance()||!targetFilter.accept(this,player,target)) {
			return false;
		}			
		
		// Player
		if (target.isPlayer()) {
			return true;
		}

		// Permanent
		if (target.isPermanent()) {
			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.getController().controlsPermanent(permanent);
		}
		
		// Card
		if (target instanceof MagicCard) {
			// Card in graveyard
			if (targetFilter.acceptType(MagicTargetType.Graveyard)&&player.getGraveyard().contains(target)) {
				return true;
			}
					
			// Card in opponent's graveyard
			if (targetFilter.acceptType(MagicTargetType.OpponentsGraveyard)&&getOpponent(player).getGraveyard().contains(target)) {
				return true;
			}
			
			return false;
		}
				
		// Item on stack
		if (target instanceof MagicItemOnStack) {
			return stack.contains(target);
		}
				
		return false;
	}
	
	public boolean isLegalTarget(final MagicPlayer player,final MagicSource source,final MagicTargetChoice targetChoice,final MagicTarget target) {
		
		if (filterTarget(player,targetChoice.getTargetFilter(),target)) {
			return !targetChoice.isTargeted()||target.isValidTarget(this,source);
		}
		return false;
	}
	
	// ***** TRIGGERS *****
	
	/** Executes triggers immediately when they have no choices, otherwise ignore them. */
	public void setImmediate(final boolean immediate) {
		
		this.immediate=immediate;
	}
	
	public MagicPermanentTrigger addTrigger(final MagicPermanent permanent,final MagicTrigger trigger) {

		final long id=createIdentifier(MagicIdentifierType.PermanentTrigger);
		final MagicPermanentTrigger permanentTrigger=new MagicPermanentTrigger(id,permanent,trigger);
		triggers.get(trigger.getType()).add(permanentTrigger);
		return permanentTrigger;
	}
		
	public void addTrigger(MagicPermanentTrigger permanentTrigger) {

		triggers.get(permanentTrigger.getTrigger().getType()).add(permanentTrigger);
	}

	public MagicPermanentTrigger addTurnTrigger(final MagicPermanent permanent,final MagicTrigger trigger) {
		
		final MagicPermanentTrigger permanentTrigger=addTrigger(permanent,trigger);
		turnTriggers.add(permanentTrigger);
		return permanentTrigger;
	}
	
	public void addTurnTriggers(final List<MagicPermanentTrigger> triggersList) {
		
		for (final MagicPermanentTrigger permanentTrigger : triggersList) {
			
			triggers.get(permanentTrigger.getTrigger().getType()).add(permanentTrigger);
		}
		turnTriggers.addAll(triggersList);
	}
	
	public void removeTurnTrigger(final MagicPermanentTrigger permanentTrigger) {
		
		triggers.get(permanentTrigger.getTrigger().getType()).remove(permanentTrigger);
		turnTriggers.remove(permanentTrigger);
	}

	public List<MagicPermanentTrigger> removeTurnTriggers() {
		
		if (turnTriggers.isEmpty()) {
			return Collections.<MagicPermanentTrigger>emptyList();
		}
		final MagicPermanentTriggerList removedTriggers=new MagicPermanentTriggerList(turnTriggers);
		for (final MagicPermanentTrigger permanentTrigger : turnTriggers) {
			
			triggers.get(permanentTrigger.getTrigger().getType()).remove(permanentTrigger);
		}
		turnTriggers.clear();
		return removedTriggers;
	}
	
	public void removeTriggers(final MagicPermanent permanent,final Collection<MagicPermanentTrigger> removedTriggers) {

		for (final MagicTriggerType type : triggers.keySet()) {
			
			for (final Iterator<MagicPermanentTrigger> iterator=triggers.get(type).iterator();iterator.hasNext();) {

				final MagicPermanentTrigger permanentTrigger=iterator.next();
				if (permanentTrigger.getPermanent()==permanent) {
					iterator.remove();
					if (removedTriggers!=null) {
						removedTriggers.add(permanentTrigger);
					}
				}
			}
		}
	}

	public void executeTrigger(final MagicTrigger trigger,final MagicPermanent permanent,final MagicSource source,final Object data) {

		final MagicEvent event=trigger.executeTrigger(this,permanent,data);
		if (event!=null) {
			if (immediate) {
				if (!event.hasChoice()) {
					event.executeEvent(this,MagicEvent.NO_CHOICE_RESULTS);
				}				
			} else if (trigger.usesStack()) {
				doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(source,event)));
			} else {
				addEvent(event);		
			}
		}
	}
	
	public void executeTrigger(final MagicTriggerType type,final Object data) {
		
		final SortedSet<MagicPermanentTrigger> typeTriggers=triggers.get(type);
		if (typeTriggers.isEmpty()) {
			return;
		}
		
		final Collection<MagicPermanentTrigger> copiedTriggers=new ArrayList<MagicPermanentTrigger>(typeTriggers);
		
		for (final MagicPermanentTrigger permanentTrigger : copiedTriggers) {

			final MagicPermanent permanent=permanentTrigger.getPermanent();
			executeTrigger(permanentTrigger.getTrigger(),permanent,permanent,data);
		}
	}
}
