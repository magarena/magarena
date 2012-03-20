package magic.model;

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
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.mstatic.MagicPermanentStaticMap;
import magic.model.mstatic.MagicLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class MagicGame {

	public static final boolean LOSE_DRAW_EMPTY_LIBRARY=true;
	private static final int LOSING_POISON=10;
	private static final long ID_FACTOR=31;
    
    private static int COUNT = 0;
    private static MagicGame INSTANCE;
	
	private final MagicDuel duel;
	private final MagicPlayer players[];
	private final MagicPermanentTriggerMap triggers;
	private final MagicPermanentTriggerList turnTriggers;
	private final MagicPermanentStaticMap statics;
	private final MagicCardList exiledUntilEndOfTurn;
	private final MagicEventQueue events;
	private final MagicStack stack;
	private final MagicPlayer scorePlayer;
	private final boolean sound;
	//private long identifiers[];
	private int score=0;
	private int turn=1;
	private int startTurn=0;
	private int mainPhaseCount=100000000;
	private int landPlayed=0;
	private int spellsPlayed = 0;
	private boolean creatureDiedThisTurn = false;
	private boolean priorityPassed=false;
	private int priorityPassedCount=0;
    private boolean skipTurn=false;
	private boolean stateCheckRequired=false;
	private boolean artificial;
	private boolean fastChoices=false;
	private boolean immediate=false;
    private boolean disableLog = false; 
	private MagicPlayer visiblePlayer;
	private MagicPlayer turnPlayer;
	private MagicPlayer losingPlayer = MagicPlayer.NONE;
	private final MagicGameplay gameplay;
	private MagicPhase phase;
	private MagicStep step;
    private final MagicPayedCost payedCost;	
    private final MagicActionList actions;	
	private MagicActionList undoPoints;
	private MagicLogBook logBook;
	private MagicLogMessageBuilder logMessageBuilder;
    private long[] keys;
    private long time = 1000000;


    public static MagicGame getInstance() {
        return INSTANCE;
    }
    
    static int getCount() {
        return COUNT;
    }

    static MagicGame create(
            final MagicDuel duel,
            final MagicGameplay gameplay,
            final MagicPlayer players[],
            final MagicPlayer startPlayer,
            final boolean sound) {
        COUNT++;
        INSTANCE = new MagicGame(duel, gameplay, players, startPlayer, sound);
        return INSTANCE;
    }

	private MagicGame(
            final MagicDuel duel,
            final MagicGameplay gameplay,
            final MagicPlayer players[],
            final MagicPlayer startPlayer,
            final boolean sound) {

		artificial=false;
		this.duel=duel;
		this.gameplay=gameplay;
		this.players=players;
		this.sound=sound;
		
        triggers=new MagicPermanentTriggerMap();
		turnTriggers=new MagicPermanentTriggerList();
        statics = new MagicPermanentStaticMap();
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
       
        //copy the reference, these are singletons
		this.duel=game.duel;
		this.gameplay=game.gameplay;
		this.phase=game.phase;
		this.step=game.step;

        //copying primitives, array of primitive
        this.time = game.time;
        this.turn = game.turn;
		this.startTurn = game.startTurn;
		this.landPlayed = game.landPlayed;
		this.spellsPlayed = game.spellsPlayed;
		this.creatureDiedThisTurn = game.creatureDiedThisTurn;
		this.priorityPassed = game.priorityPassed;
        this.priorityPassedCount = game.priorityPassedCount;
		this.stateCheckRequired = game.stateCheckRequired;
		
        //copied and stored in copyMap
        final MagicCopyMap copyMap=new MagicCopyMap();		
		this.players=copyMap.copyObjects(game.players,MagicPlayer.class);		
		this.scorePlayer=copyMap.copy(scorePlayer);
		this.visiblePlayer=copyMap.copy(game.visiblePlayer);
		this.turnPlayer=copyMap.copy(game.turnPlayer);
        //this.losingPlayer
		
        //construct a new object using copyMap to copy internals
        this.events=new MagicEventQueue(copyMap, game.events);
		this.stack=new MagicStack(copyMap, game.stack);
		this.triggers=new MagicPermanentTriggerMap(copyMap, game.triggers);
		this.statics=new MagicPermanentStaticMap(copyMap, game.statics);
		this.payedCost=new MagicPayedCost(copyMap, game.payedCost);
		this.exiledUntilEndOfTurn=new MagicCardList(copyMap, game.exiledUntilEndOfTurn);
       
        //construct a new object
        this.turnTriggers=new MagicPermanentTriggerList(triggers, game.turnTriggers);
   
        //the following are NOT copied when game state is cloned
        //fastChoices
        //immediate
        //skipTurn
        //mainPhaseCount
	
        //score is RESET to zero
		this.score=0;
        
        //historical items are cleared
        this.actions=new MagicActionList();
        this.disableLog = true;
		/*
        this.undoPoints=null;
		this.logBook=null;
		this.logMessageBuilder=null;
        */
	}
	
    public void setSkipTurn(final boolean skip) {
        skipTurn = skip;
    }

    private boolean getSkipTurn() {
        return skipTurn;
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

    public long getUniqueId() {
        time++;
        return time;
    }

    //follow factors in MagicMarkerAction
    public long getGameId() {
		keys = new long[] { 
            turn,
            phase.hashCode(),
            step.hashCode(),
            turnPlayer.getIndex(),
            landPlayed,
            priorityPassedCount,
            (priorityPassed ? 1L : -1L),
            (stateCheckRequired ? 1L : -1L),
            getPayedCost().getX(),
            stack.getItemsId(),
            events.getEventsId(),
            //time,
            //identifiers[0],
            //identifiers[1],
            //identifiers[2],
            players[0].getPlayerId(),
            players[1].getPlayerId(),
        };
		return magic.MurmurHash3.hash(keys);
    }

    public String toString() {
        return "GAME: " +
               "t=" + turn + " " + 
               "p=" + phase.getType() + " " + 
               "s=" + step + " " + 
               "tp=" + turnPlayer.getIndex() + " " +
               "lp=" + landPlayed + " " +
               "ppc=" + priorityPassedCount + " " +
               "pp=" + priorityPassed + " " +
               "sc=" + stateCheckRequired + " " +
               "x=" + getPayedCost().getX() + " " +
               "e=" + events.size() + " " +
               "s=" + stack.size();
    }
    
    public String getIdString() {
        final StringBuilder sb = new StringBuilder(toString());
        sb.append('\n');
        sb.append(keys[0]);
        for (int i = 1; i < keys.length; i++) {
            sb.append(' ');
            sb.append(keys[i]);
        }
        sb.append('\n');
        sb.append(players[0].getIdString());
        sb.append('\n');
        sb.append(players[1].getIdString());
        return sb.toString();
    }
	
	public long getGameId(final int pruneScore) {
		long id=0; 
        id = id*ID_FACTOR + turn;
        id = id*ID_FACTOR + phase.getType().ordinal();
        id = id*ID_FACTOR + score + pruneScore;
		id = players[0].getPlayerId(id);
		id = players[1].getPlayerId(id);
		return id;
	}
	
	public static boolean canSkipSingleChoice() {
		return GeneralConfig.getInstance().getSkipSingle();
	}
	
	public static boolean canSkipSingleManaChoice() {
		return GeneralConfig.getInstance().getSkipSingle();
	}

    //human is declaring blockers, skip if AI is not attacking
	public boolean canSkipDeclareBlockersSingleChoice() {
		return GeneralConfig.getInstance().getSkipSingle() && turnPlayer.getNrOfAttackers() == 0;
	}		
	
	public boolean canAlwaysPass() {
		if (GeneralConfig.getInstance().getAlwaysPass()) {
			return phase.getType() == MagicPhaseType.Draw || 
                   phase.getType() == MagicPhaseType.BeginOfCombat;
		}
		return false;
	}
	
	private int getArtificialLevel() {
		return duel.getDifficulty();
	}
	
    public int getArtificialLevel(final int idx) {
		return duel.getDifficulty(idx);
	}

	public boolean isArtificial() {
		return artificial;
	}
	
    public void setArtificial(final boolean art) {
		artificial = art;
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
	
	public void changePhase(final MagicPhase aPhase) {
		this.phase=aPhase;
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

	/** Tells gameplay that it can skip certain parts during AI processing. */
	public boolean canSkip() {
        return stack.isEmpty() && artificial;
	}
	
	public boolean isFinished() {
        return losingPlayer.isValid() || mainPhaseCount <= 0;
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

    /*
	public long createIdentifier(final MagicIdentifierType type) {
		return identifiers[type.ordinal()]++;
	}
	
	public void releaseIdentifier(final MagicIdentifierType type) {
		identifiers[type.ordinal()]--;
	}
	
	public void setIdentifiers(final long identifiers[]) {
		this.identifiers=identifiers;
	}
	
	public long[] getIdentifiers() {
		return Arrays.copyOf(identifiers,identifiers.length);
	}
    */

    public int getNumActions() {
        return actions.size();
    }
		
	public void startActions() {
		doAction(new MagicMarkerAction());
	}
		
	public void doAction(final MagicAction action) {
		actions.add(action);
		action.doAction(this);

        //performing actions update the score
		score += action.getScore(scorePlayer);
	}
		
	public void undoActions() {
        //undo each action up to and including the first MagicMarkerAction
        MagicAction action;
		do {
			action = actions.removeLast();
			action.undoAction(this);
		} while (!(action instanceof MagicMarkerAction));
	}
	
	public void undoAllActions() {
		while (!actions.isEmpty()) {
			actions.removeLast().undoAction(this);
		}
	}
	
	Collection<MagicAction> getActions() {
		return actions;
	}
	
	public void createUndoPoint() {
		final MagicAction markerAction=new MagicMarkerAction();
		doAction(markerAction);
		doAction(new MagicLogMarkerAction());
		undoPoints.addLast(markerAction);
	}
		
	public void gotoLastUndoPoint() {
		final MagicAction markerAction = undoPoints.removeLast();
		MagicAction action;
        do {
			action = actions.removeLast();
			action.undoAction(this);
        } while (!(action == markerAction));
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
        if (disableLog) {
            return;
        }
        logMessageBuilder.logMessages();
	}

	private void logAppendEvent(final MagicEvent event,final Object choiceResults[]) {
	    if (disableLog) {
            return;
        }
		final String message=event.getDescription(choiceResults);
        if (message.length() == 0) {
            return;
        }
        logMessageBuilder.appendMessage(event.getPlayer(),message);
	}
	
	public void logAppendMessage(final MagicPlayer player,final String message) {
        if (disableLog) {
            return;
        }
        logMessageBuilder.appendMessage(player,message);
	}
	
	public void logMessage(final MagicPlayer player,final String message) {
        if (disableLog) {
            return;
        }
        logBook.add(new MagicMessage(this,player,message));
	}
	
	public void logAttackers(final MagicPlayer player,final MagicDeclareAttackersResult result) {
        if (disableLog || result.isEmpty()) {
            return;
        }
        final SortedSet<String> names=new TreeSet<String>();
        for (final MagicPermanent attacker : result) {
            names.add(attacker.getName());
        }
        final String playerName = player.getName();
        final StringBuilder builder = new StringBuilder(playerName + " attacks with ");
        MagicMessage.addNames(builder,names);
        builder.append('.');
        logBook.add(new MagicMessage(this,player,builder.toString()));
	}
	
	public void logBlockers(final MagicPlayer player,final MagicDeclareBlockersResult result) {
        if (disableLog) {
            return;
        }
        final SortedSet<String> names=new TreeSet<String>();
        for (final MagicCombatCreature[] creatures : result) {
            for (int index=1;index<creatures.length;index++) {
                names.add(creatures[index].getName());
            }
        }
        if (names.isEmpty()) {
            return;
        }
        final String playerName = player.getName();
        final StringBuilder builder=new StringBuilder(playerName + " blocks with ");
        MagicMessage.addNames(builder,names);
        builder.append('.');
        logBook.add(new MagicMessage(this,player,builder.toString()));
	}

	public void executeEvent(final MagicEvent event,final Object choiceResults[]) {
        if (choiceResults == null) {
            throw new RuntimeException("choiceResults is null");
        }
        
        logAppendEvent(event,choiceResults);
    
        // Payed cost.
        if (choiceResults.length==1) {
            payedCost.set(choiceResults[0]);
        }
        
        event.executeEvent(this,choiceResults);
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
	
	public MagicDuel getDuel() {
		return duel;
	}
	
	public void advanceDuel() {
		duel.advance(losingPlayer!=players[0],this);
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
	
	private void setVisiblePlayer(final MagicPlayer visiblePlayer) {
		this.visiblePlayer = visiblePlayer;
	}
	
	public MagicPlayer getVisiblePlayer() {
		return visiblePlayer;
	}
		
	public void setTurnPlayer(final MagicPlayer turnPlayer) {
		this.turnPlayer = turnPlayer;
	}
		
	public MagicPlayer getTurnPlayer() {
		return turnPlayer;
	}
	
	public MagicPlayer getPriorityPlayer() {
		return step == MagicStep.ActivePlayer ? turnPlayer : getOpponent(turnPlayer);
	}
		
	public MagicPlayer getScorePlayer() {
		return scorePlayer;
	}
	
	public void setLosingPlayer(final MagicPlayer player) {
		losingPlayer = player;
	}
	
	public MagicPlayer getLosingPlayer() {
		return losingPlayer;
	}
			
	public boolean hasTurn(final MagicPlayer player) {
		return player == turnPlayer;
	}
	
	public int getCount(final int cardDefinitionIndex) {
		return players[0].getCount(cardDefinitionIndex) + 
               players[1].getCount(cardDefinitionIndex);
	}
	
	public int getOtherPlayerCount(final int cardDefinitionIndex,final MagicPlayer player) {
		if (players[0] != player) {
			return players[0].getCount(cardDefinitionIndex);
		} else {
			return players[1].getCount(cardDefinitionIndex);
		}
	}
	
	public int getNrOfPermanents(final MagicType type) {
		return players[0].getNrOfPermanentsWithType(type,this) + 
               players[1].getNrOfPermanentsWithType(type,this);
	}
			
	public boolean canPlaySorcery(final MagicPlayer controller) {
		return phase.getType().isMain() && 
               stack.isEmpty() && 
               turnPlayer == controller;
	}
	
	public boolean canPlayLand(final MagicPlayer controller) {
		return landPlayed < 1 && canPlaySorcery(controller);
	}

    public int getLandPlayed() {
        return landPlayed;
    }
	
	public void incLandPlayed() {
		this.landPlayed++;
	}
	
    public void decLandPlayed() {
		this.landPlayed--;
	}
    
    public void resetLandPlayed() {
		this.landPlayed = 0;
	}

    public void setLandPlayed(final int lp) {
		this.landPlayed = lp;
	}
    
	public int getSpellsPlayed() {
		return spellsPlayed;
	}
	
	public void setSpellsPlayed(int spells) {
		spellsPlayed = spells;
	}
    public boolean getCreatureDiedThisTurn() {
    	return creatureDiedThisTurn;
    }
    
    public void setCreatureDiedThisTurn(boolean died) {
    	this.creatureDiedThisTurn = died;
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
		return new MagicPermanent(getUniqueId(),card,controller);
	}
	
	public MagicCardList getExiledUntilEndOfTurn() {
		return exiledUntilEndOfTurn;
	}
	
	public void setStateCheckRequired(final boolean required) {
		stateCheckRequired = required;
	}
	
	public void setStateCheckRequired() {
		stateCheckRequired = true;
	}
	
	public boolean getStateCheckRequired() {
		return stateCheckRequired;
	}
	
	public void checkState() {
		while (stateCheckRequired) {
			stateCheckRequired = false;
           
            //accumulate the state-based actions
            final List<MagicAction> stateBasedActions = new ArrayList<MagicAction>(100);
		
            // Check if a player has lost
            for (final MagicPlayer player : players) {
            	if (player.getLife() <= 0) {
            		stateBasedActions.add(new MagicLoseGameAction(player,MagicLoseGameAction.LIFE_REASON));
            	}
            	if (player.getPoison() >= LOSING_POISON) {
            		stateBasedActions.add(new MagicLoseGameAction(player,MagicLoseGameAction.POISON_REASON));
            	}
            }

            // Check permanents' state
            for (final MagicPlayer player : players) {
            	for (final MagicPermanent permanent : player.getPermanents()) {
            		permanent.checkState(this, stateBasedActions);
            	}
            }
            
            //perform all the actions at once
            for (final MagicAction action : stateBasedActions) {
                doAction(action);
            }

            //some action may set stateCheckRequired to true, if so loop again
		}
	}

	public void checkLegendRule(final MagicPermanent permanent) {
		final MagicCardDefinition cardDefinition=permanent.getCardDefinition();
		if (cardDefinition.hasType(MagicType.Legendary) && 
            getCount(cardDefinition.getIndex()) > 1) {
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
			if (obj != null && obj instanceof MagicMappable) {
				mappedData[index]=((MagicMappable)obj).map(this);
			} else {
				mappedData[index]=obj;
			}
		}	
		return mappedData;
	}
	
	// ***** TARGETS *****

	private List<MagicTarget> filterTargets(
            final MagicPlayer player,
            final MagicTargetFilter targetFilter,
            final MagicTargetHint targetHint) {

		final List<MagicTarget> targets=new ArrayList<MagicTarget>();
		
		// Players
		if (targetFilter.acceptType(MagicTargetType.Player)) {
			for (final MagicPlayer targetPlayer : players) {
				if (targetFilter.accept(this,player,targetPlayer) &&
                    targetHint.accept(player,targetPlayer)) {
					targets.add(targetPlayer);
				}				
			}
		}
		
		// Permanents
		if (targetFilter.acceptType(MagicTargetType.Permanent)) {
			for (final MagicPlayer controller : players) {
				for (final MagicPermanent targetPermanent : controller.getPermanents()) {
					if (targetFilter.accept(this,player,targetPermanent) && 
                        targetHint.accept(player,targetPermanent)) {
						targets.add(targetPermanent);
					}
				}
			}
		}		

		// Items on stack
		if (targetFilter.acceptType(MagicTargetType.Stack)) {
			for (final MagicItemOnStack targetItemOnStack : stack) {
				if (targetFilter.accept(this,player,targetItemOnStack) && 
                    targetHint.accept(player,targetItemOnStack)) {
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
		
		// Cards in hand
		if (targetFilter.acceptType(MagicTargetType.Hand)) {
			for (final MagicCard targetCard : player.getHand()) {
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

	public boolean hasLegalTargets(
            final MagicPlayer player,
            final MagicSource source,
            final MagicTargetChoice targetChoice,
            final boolean hints) {
	
		final Collection<MagicTarget> targets = filterTargets(
                player,
                targetChoice.getTargetFilter(),
                targetChoice.getTargetHint(hints));

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
	
	public List<Object> getLegalTargets(
            final MagicPlayer player,
            final MagicSource source,
			final MagicTargetChoice targetChoice,
            final MagicTargetHint targetHint) {

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
	
	private boolean filterTarget(final MagicPlayer player,final MagicTargetFilter targetFilter,final MagicTarget target) {
		if (target==null || 
            target==MagicTargetNone.getInstance() || 
            !targetFilter.accept(this,player,target)) {
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
			if (targetFilter.acceptType(MagicTargetType.Graveyard) && 
                player.getGraveyard().contains(target)) {
				return true;
			}
					
			// Card in opponent's graveyard
			if (targetFilter.acceptType(MagicTargetType.OpponentsGraveyard) && 
                getOpponent(player).getGraveyard().contains(target)) {
				return true;
			}
			
			// Card in hand
			if (targetFilter.acceptType(MagicTargetType.Hand) && 
				player.getHand().contains(target)) {
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
	
	public boolean isLegalTarget(
            final MagicPlayer player,
            final MagicSource source,
            final MagicTargetChoice targetChoice,
            final MagicTarget target) {
		
		if (filterTarget(player,targetChoice.getTargetFilter(),target)) {
			return !targetChoice.isTargeted()||target.isValidTarget(this,source);
		}
		return false;
	}
	
    // ***** STATICS *****
	
    public void addCardStatics(final MagicPermanent permanent) {
        for (final MagicStatic mstatic : permanent.getCardDefinition().getStatics()) {
            addStatic(permanent, mstatic);
        }
	}
    
    public Collection<MagicPermanentStatic> removeCardStatics(final MagicPermanent permanent) {
        return statics.remove(permanent, permanent.getCardDefinition().getStatics());
	}
    
    public void addStatic(final MagicPermanent permanent, final MagicStatic mstatic) {
        addStatic(new MagicPermanentStatic(getUniqueId(),permanent,mstatic));
	}
    
    public void addStatic(final MagicPermanentStatic permanentStatic) {
		statics.add(permanentStatic);
	}

    public void addStatics(final Collection<MagicPermanentStatic> aStatics) {
        for (final MagicPermanentStatic mpstatic : aStatics) {
            addStatic(mpstatic);
        }
    }
    
    public Collection<MagicPermanentStatic> getStatics(MagicLayer layer) {
        return statics.get(layer);
	}
    
    public Collection<MagicPermanentStatic> removeTurnStatics() {
        return statics.removeTurn();
	}
	
    public Collection<MagicPermanentStatic> removeAllStatics(final MagicPermanent permanent) {
        return statics.remove(permanent);
	}
    
    public void removeStatic(final MagicPermanent permanent,final MagicStatic mstatic) {
        statics.remove(permanent, mstatic);
	}

	
	// ***** TRIGGERS *****
	
	/** Executes triggers immediately when they have no choices, otherwise ignore them. */
	public void setImmediate(final boolean immediate) {
		this.immediate=immediate;
	}
	
	public void addTriggers(final MagicPermanent permanent) {
        for (final MagicTrigger trigger : permanent.getCardDefinition().getTriggers()) {
            addTrigger(permanent, trigger);
        }
	}

	public MagicPermanentTrigger addTrigger(final MagicPermanent permanent, final MagicTrigger trigger) {
        return addTrigger(new MagicPermanentTrigger(getUniqueId(),permanent,trigger));
	}
		
	public MagicPermanentTrigger addTrigger(final MagicPermanentTrigger permanentTrigger) {
		triggers.add(permanentTrigger);
        return permanentTrigger;
	}

	public MagicPermanentTrigger addTurnTrigger(final MagicPermanent permanent,final MagicTrigger trigger) {
		final MagicPermanentTrigger permanentTrigger = addTrigger(permanent,trigger);
		turnTriggers.add(permanentTrigger);
		return permanentTrigger;
	}
	
	public void addTurnTriggers(final List<MagicPermanentTrigger> triggersList) {
		for (final MagicPermanentTrigger permanentTrigger : triggersList) {
            addTrigger(permanentTrigger);
		}
		turnTriggers.addAll(triggersList);
	}
	
	public void removeTurnTrigger(final MagicPermanentTrigger permanentTrigger) {
		triggers.remove(permanentTrigger);
		turnTriggers.remove(permanentTrigger);
	}

	public List<MagicPermanentTrigger> removeTurnTriggers() {
		if (turnTriggers.isEmpty()) {
			return Collections.<MagicPermanentTrigger>emptyList();
		}
		final MagicPermanentTriggerList removedTriggers = new MagicPermanentTriggerList(turnTriggers);
		for (final MagicPermanentTrigger permanentTrigger : removedTriggers) {
            removeTurnTrigger(permanentTrigger);
		}
		return removedTriggers;
	}
	
	public Collection<MagicPermanentTrigger> removeTriggers(final MagicPermanent permanent) {
        return triggers.remove(permanent);
	}

	public void executeTrigger(
            final MagicTrigger trigger,
            final MagicPermanent permanent,
            final MagicSource source,
            final Object data) {
        @SuppressWarnings("unchecked")
		final MagicEvent event=trigger.executeTrigger(this,permanent,data);
		if (event.isValid()) {
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
