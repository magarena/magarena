package magic.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import magic.data.GeneralConfig;
import magic.data.settings.BooleanSetting;
import magic.exception.GameException;
import magic.model.action.AddEventAction;
import magic.model.action.AddFirstEventAction;
import magic.model.action.DequeueTriggerAction;
import magic.model.action.EnqueueTriggerAction;
import magic.model.action.ExecuteFirstEventAction;
import magic.model.action.LogMarkerAction;
import magic.model.action.MagicAction;
import magic.model.action.MagicActionList;
import magic.model.action.MarkerAction;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveFromPlayAction;
import magic.model.choice.MagicCombatCreature;
import magic.model.choice.MagicDeclareAttackersResult;
import magic.model.choice.MagicDeclareBlockersResult;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventQueue;
import magic.model.event.MagicUniquenessEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.mstatic.MagicPermanentStaticMap;
import magic.model.mstatic.MagicStatic;
import magic.model.phase.MagicGameplay;
import magic.model.phase.MagicPhase;
import magic.model.phase.MagicPhaseType;
import magic.model.phase.MagicStep;
import magic.model.stack.MagicStack;
import magic.model.target.MagicLegendaryCopiesFilter;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetNone;
import magic.model.trigger.AtEndOfTurnTrigger;
import magic.model.trigger.DamageIsDealtTrigger;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicPermanentTriggerMap;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.trigger.OtherEntersBattlefieldTrigger;
import magic.model.trigger.PreventDamageTrigger;
import magic.model.trigger.AtUpkeepTrigger;
import magic.ui.MagicSound;

public class MagicGame {

    public static final boolean LOSE_DRAW_EMPTY_LIBRARY=true;
    private static final long ID_FACTOR=31;

    private static int COUNT;
    private static MagicGame INSTANCE;

    private final MagicDuel duel;
    private final MagicPlayer[] players;
    private MagicPermanentTriggerMap triggers;
    private final MagicPermanentTriggerMap additionalTriggers;
    private final MagicPermanentStaticMap statics;
    private final MagicCardList exiledUntilEndOfTurn;
    private final MagicEventQueue events;
    private final MagicStack stack;
    private final MagicStack pendingStack;
    private final MagicPlayer scorePlayer;
    private final MagicGameplay gameplay;
    private final MagicActionList actions;
    private final MagicActionList delayedActions;
    private int score;
    private int turn=1;
    private int startTurn;
    private int mainPhaseCount=100000000;
    private int landsPlayed;
    private int maxLands;
    private int priorityPassedCount;
    private boolean creatureDiedThisTurn;
    private boolean priorityPassed;
    private MagicPhaseType skipTurnTill = MagicPhaseType.Mulligan;
    private boolean stateCheckRequired;
    private boolean artificial;
    private boolean immediate;
    private boolean disableLog;
    private MagicPlayer turnPlayer;
    private MagicPlayer losingPlayer = MagicPlayer.NONE;
    private MagicPhase phase;
    private MagicStep step;
    private MagicPayedCost payedCost;
    private MagicActionList undoPoints;
    private MagicLogBook logBook;
    private MagicLogMessageBuilder logMessageBuilder;
    private MagicSource activeSource = MagicSource.NONE;
    private long[] keys;
    private long stateId;
    private long time = 1000000;
    private boolean isConceded = false;

    // affects options available to AI for each choice
    private boolean fastMana = false;
    private boolean fastTarget = false;
    private boolean fastBlocker = false;
    private boolean fastGameplay = false;
    private boolean hintTiming = true;
    private boolean hintPriority = true;
    private boolean hintTarget = true;

    private final long startTimeMilli = Instant.now().toEpochMilli();

    public static MagicGame getInstance() {
        return INSTANCE;
    }

    static int getCount() {
        return COUNT;
    }

    static MagicGame create(final MagicDuel duel, final MagicGameplay gameplay, final MagicPlayer[] players, final MagicPlayer startPlayer) {
        COUNT++;
        INSTANCE = new MagicGame(duel, gameplay, players, startPlayer);
        return INSTANCE;
    }

    private MagicGame(final MagicDuel aDuel, final MagicGameplay aGameplay, final MagicPlayer[] aPlayers, final MagicPlayer startPlayer) {

        artificial=false;
        duel = aDuel;
        gameplay = aGameplay;
        players = aPlayers;
        for (final MagicPlayer player : players) {
            player.setGame(this);
        }

        triggers=new MagicPermanentTriggerMap();
        additionalTriggers=new MagicPermanentTriggerMap();
        statics = new MagicPermanentStaticMap();
        exiledUntilEndOfTurn=new MagicCardList();
        events=new MagicEventQueue();
        stack=new MagicStack();
        pendingStack=new MagicStack();
        scorePlayer=players[0];
        turnPlayer=startPlayer;
        actions=new MagicActionList();
        delayedActions=new MagicActionList();
        undoPoints=new MagicActionList();
        logBook=new MagicLogBook();
        logMessageBuilder=new MagicLogMessageBuilder(this);
        payedCost=new MagicPayedCost();
        changePhase(gameplay.getStartPhase(this));
    }

    public MagicGame(final MagicGame game,final MagicPlayer aScorePlayer) {

        artificial=true;

        //copy the reference, these are singletons
        duel=game.duel;
        gameplay=game.gameplay;
        phase=game.phase;
        step=game.step;

        //copying primitives, array of primitive
        time = game.time;
        turn = game.turn;
        startTurn = game.startTurn;
        landsPlayed = game.landsPlayed;
        maxLands = game.maxLands;
        creatureDiedThisTurn = game.creatureDiedThisTurn;
        priorityPassed = game.priorityPassed;
        priorityPassedCount = game.priorityPassedCount;
        stateCheckRequired = game.stateCheckRequired;

        //copied and stored in copyMap
        final MagicCopyMap copyMap=new MagicCopyMap();
        players=copyMap.copyObjects(game.players,MagicPlayer.class);
        for (final MagicPlayer player : players) {
            player.setGame(this);
        }
        scorePlayer=copyMap.copy(aScorePlayer);
        turnPlayer=copyMap.copy(game.turnPlayer);
        losingPlayer=copyMap.copy(game.losingPlayer);
        payedCost=copyMap.copy(game.payedCost);
        activeSource=copyMap.copy(game.activeSource);

        //construct a new object using copyMap to copy internals
        events=new MagicEventQueue(copyMap, game.events);
        stack=new MagicStack(copyMap, game.stack);
        pendingStack=new MagicStack(copyMap, game.pendingStack);
        triggers=new MagicPermanentTriggerMap(copyMap, game.triggers);
        additionalTriggers=new MagicPermanentTriggerMap(copyMap, game.additionalTriggers);
        statics=new MagicPermanentStaticMap(copyMap, game.statics);
        exiledUntilEndOfTurn=new MagicCardList(copyMap, game.exiledUntilEndOfTurn);

        //the following are NOT copied when game state is cloned
        //fastMana
        //fastTarget
        //fastBlocker
        //immediate
        //hintTiming
        //hintPriority
        //hintTarget
        //skipTurnTill
        //mainPhaseCount

        //score is RESET to zero
        score=0;

        //historical actions are not carried over
        actions=new MagicActionList();

        //there should be no pending actions
        assert game.delayedActions.isEmpty() : "delayedActions: " + game.delayedActions;
        delayedActions=new MagicActionList();

        //no logging
        disableLog = true;
        undoPoints=null;
        logBook=null;
        logMessageBuilder=null;
    }

    public void skipTurnTill(final MagicPhaseType skip) {
        skipTurnTill = skip;
    }

    public void clearSkipTurnTill() {
        skipTurnTill = MagicPhaseType.Mulligan;
    }

    public boolean shouldSkip() {
        return phase.getType().ordinal() < skipTurnTill.ordinal();
    }

    public void setScore(final int aScore) {
        score = aScore;
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
    public long getStateId() {
        keys = new long[] {
            turn,
            phase.hashCode(),
            step.hashCode(),
            turnPlayer.getIndex(),
            landsPlayed,
            maxLands,
            priorityPassedCount,
            creatureDiedThisTurn ? 1L : -1L,
            priorityPassed       ? 1L : -1L,
            stateCheckRequired   ? 1L : -1L,
            payedCost.getStateId(),
            stack.getStateId(),
            pendingStack.getStateId(),
            events.getStateId(),
            players[0].getStateId(),
            players[1].getStateId(),
            activeSource.getStateId(),
            triggers.getStateId(),
            statics.getStateId(),
            exiledUntilEndOfTurn.getStateId(),
        };
        stateId = MurmurHash3.hash(keys);
        return stateId;
    }

    @Override
    public String toString() {
        return "GAME: " +
               "id=" + stateId + " " +
               "t=" + turn + " " +
               "p=" + phase.getType() + " " +
               "s=" + step + " " +
               "tp=" + turnPlayer.getIndex() + " " +
               "lp=" + landsPlayed + " " +
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
            sb.append(' ').append(keys[i]);
        }
        for (MagicPlayer player : players) {
            sb.append('\n').append(player.getIdString());
        }
        return sb.toString();
    }

    public long getGameId(final int pruneScore) {
        long id=0;
        id = id*ID_FACTOR + turn;
        id = id*ID_FACTOR + phase.getType().ordinal();
        id = id*ID_FACTOR + score + pruneScore;
        for (MagicPlayer player : players) {
            id = player.getPlayerId(id);
        }
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
        if (GeneralConfig.get(BooleanSetting.ALWAYS_PASS)) {
            return phase.getType() == MagicPhaseType.Draw ||
                   phase.getType() == MagicPhaseType.BeginOfCombat;
        }
        return false;
    }

    public boolean isArtificial() {
        return artificial;
    }

    public boolean isReal() {
        return !artificial;
    }

    public void setArtificial(final boolean art) {
        artificial = art;
    }

    public boolean getFastMana() {
        return fastMana;
    }

    public void setFastMana(final boolean v) {
        fastMana = v;
    }

    public boolean getFastTarget() {
        return fastTarget;
    }

    public void setFastTarget(final boolean v) {
        fastTarget = v;
    }

    public boolean getFastBlocker() {
        return fastBlocker;
    }

    public void setFastBlocker(final boolean v) {
        fastBlocker = v;
    }

    public boolean getFastGameplay() {
        return fastGameplay;
    }

    public void setFastGameplay(final boolean v) {
        fastGameplay = v;
    }

    public void setFastChoices(final boolean v) {
        fastMana = v;
        fastTarget = v;
        fastBlocker = v;
        fastGameplay = v;
    }

    public boolean getHintTiming() {
        return hintTiming;
    }

    public void setHintTiming(final boolean v) {
        hintTiming = v;
    }

    public boolean getHintPriority() {
        return hintPriority;
    }

    public void setHintPriority(final boolean v) {
        hintPriority = v;
    }

    public boolean getHintTarget() {
        return hintTarget;
    }

    public void setHintTarget(final boolean v) {
        hintTarget = v;
    }

    public void setTurn(final int aTurn) {
        turn = aTurn;
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

    public void setPhase(final MagicPhase aPhase) {
        phase = aPhase;
    }

    public void nextPhase() {
        changePhase(gameplay.getNextPhase(this));
    }

    private void changePhase(final MagicPhase aPhase) {
        phase = aPhase;
        step = MagicStep.Begin;
        priorityPassedCount = 0;
        for (MagicPlayer player : players) {
            player.getActivationPriority().clear();
        }
    }

    public MagicPhase getPhase() {
        return phase;
    }

    public void executePhase() {
        phase.executePhase(this);
    }

    public boolean isPhase(final MagicPhaseType type) {
        return phase.getType()==type;
    }

    public boolean isMainPhase() {
        return phase.getType().isMain();
    }

    public boolean isCombatPhase() {
        return phase.getType().isCombat();
    }

    public void setStep(final MagicStep aStep) {
        step = aStep;
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

    public void resetPayedCost() {
        payedCost = new MagicPayedCost();
    }

    public void setPayedCost(final MagicPayedCost aPayedCost) {
        payedCost = aPayedCost;
    }

    public MagicPayedCost getPayedCost() {
        return new MagicPayedCost(payedCost);
    }

    /** Tells gameplay that it can skip certain parts during AI processing. */
    public boolean canSkip() {
        return stack.isEmpty() && artificial && fastGameplay;
    }

    public boolean isFinished() {
        return losingPlayer.isValid() || mainPhaseCount <= 0;
    }

    public MagicLogBook getLogBook() {
        return logBook;
    }

    public void hideHiddenCards() {
        getOpponent(scorePlayer).setHandToUnknown();
        for (final MagicPlayer player : players) {
            player.getLibrary().setAIKnown(false);
        }
    }

    public void showRandomizedHiddenCards() {
        getOpponent(scorePlayer).showRandomizedHandAndLibrary();
        scorePlayer.getLibrary().shuffle(MagicRandom.nextRNGInt());
        scorePlayer.getLibrary().setAIKnown(true);
    }

    public Collection<MagicAction> getActions() {
        return actions;
    }

    public int getNumActions() {
        return actions.size();
    }

    public void addDelayedAction(final MagicAction action) {
        delayedActions.add(action);
    }

    public void doAction(final MagicAction action) {
        if (!action.isLegal(this)) {
            return;
        }
        actions.add(action);
        try {
            action.doAction(this);
        } catch (Throwable ex) {
            throw new GameException(ex, this);
        }
        //performing actions update the score
        score += action.getScore(scorePlayer);
    }

    public void update() {
        doDelayedActions();
        MagicPermanent.update(this);

        triggers = new MagicPermanentTriggerMap(additionalTriggers);

        // add Soulbond trigger
        triggers.add(new MagicPermanentTrigger(0, MagicPermanent.NONE, OtherEntersBattlefieldTrigger.Soulbond));

        // add Monarch triggers
        triggers.add(new MagicPermanentTrigger(1, MagicPermanent.NONE, AtEndOfTurnTrigger.Monarch));
        triggers.add(new MagicPermanentTrigger(2, MagicPermanent.NONE, DamageIsDealtTrigger.Monarch));

        // add Suspend trigger
        triggers.add(new MagicPermanentTrigger(3, MagicPermanent.NONE, AtUpkeepTrigger.Suspend));

        // prevent damage replacement
        triggers.add(new MagicPermanentTrigger(4, MagicPermanent.NONE, PreventDamageTrigger.ProtectionShield));
        triggers.add(new MagicPermanentTrigger(Long.MAX_VALUE, MagicPermanent.NONE, PreventDamageTrigger.PreventDamageShield));

        for (final MagicPlayer player : players) {
        for (final MagicPermanent perm : player.getPermanents()) {
        for (final MagicTrigger<?> trigger : perm.getTriggers()) {
            triggers.add(new MagicPermanentTrigger(getUniqueId(), perm, trigger));
        }}}

        MagicPlayer.update(this);
        MagicGame.update(this);
        doDelayedActions();
    }

    public MagicManaCost modCost(final MagicCard card, final MagicManaCost cost) {
        MagicManaCost currCost = cost;
        for (final MagicPermanentStatic mps : getStatics(MagicLayer.CostIncrease)) {
            currCost = mps.getStatic().increaseCost(mps.getPermanent(), card, currCost);
        }
        for (final MagicPermanentStatic mps : getStatics(MagicLayer.CostReduction)) {
            currCost = mps.getStatic().reduceCost(mps.getPermanent(), card, currCost);
        }
        return currCost;
    }

    public void apply(final MagicLayer layer) {
        switch (layer) {
            case Game:
                maxLands = 1;
                break;
            default:
                throw new RuntimeException("No case for " + layer + " in MagicGame.apply");
        }
    }

    private void apply(final MagicPermanent source, final MagicStatic mstatic) {
        final MagicLayer layer = mstatic.getLayer();
        switch (layer) {
            case Game:
                mstatic.modGame(source, this);
                break;
            default:
                throw new RuntimeException("No case for " + layer + " in MagicGame.apply");
        }
    }

    public static void update(final MagicGame game) {
        game.apply(MagicLayer.Game);
        for (final MagicPermanentStatic mpstatic : game.getStatics(MagicLayer.Game)) {
            final MagicStatic mstatic = mpstatic.getStatic();
            final MagicPermanent source = mpstatic.getPermanent();
            if (mstatic.accept(game, source, source)) {
                game.apply(source, mstatic);
            }
        }
    }

    private void doDelayedActions() {
        while (!delayedActions.isEmpty()) {
            final MagicAction action = delayedActions.removeFirst();
            doAction(action);
        }
    }

    public void snapshot() {
        final MagicAction markerAction=new MarkerAction();
        doAction(markerAction);
        if (!artificial) {
            doAction(new LogMarkerAction());
            undoPoints.addLast(markerAction);
        }
    }

    public void restore() {
        if (!artificial) {
            undoPoints.removeLast();
        }
        //undo each action up to and including the first MagicMarkerAction
        MagicAction action;
        do {
            action = actions.removeLast();
            try {
                action.undoAction(this);
            } catch (Throwable ex) {
                //put action back so that it shows up in report
                actions.addLast(action);
                throw new GameException(ex, this);
            }
        } while (!(action instanceof MarkerAction));
    }

    public void undoAllActions() {
        assert actions.isEmpty() : "actions: " + actions;
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

    private void logAppendEvent(final MagicEvent event,final Object[] choiceResults) {
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

    public void logAppendValue(final MagicPlayer player, final int amount) {
        if (disableLog) {
            return;
        }
        logMessageBuilder.appendMessage(player, "(" + amount + ")");
    }

    public void logAppendX(final MagicPlayer player, final int X) {
        if (disableLog) {
            return;
        }
        logMessageBuilder.appendMessage(player, "(X="+X+")");
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
        final SortedSet<String> names= new TreeSet<>();
        for (final MagicPermanent attacker : result) {
            names.add(MagicMessage.getCardToken(attacker));
        }
        final StringBuilder builder = new StringBuilder(player + " attacks with ");
        MagicMessage.addNames(builder,names);
        builder.append('.');
        logBook.add(new MagicMessage(this,player,builder.toString()));
    }

    public void logBlockers(final MagicPlayer player, final MagicDeclareBlockersResult result) {
        if (disableLog) {
            return;
        }
        final SortedSet<String> names = new TreeSet<>();
        for (final MagicCombatCreature[] creatures : result) {
            for (int index = 1; index < creatures.length; index++) {
                names.add(MagicMessage.getCardToken(creatures[index].permanent));
            }
        }
        if (names.isEmpty()) {
            return;
        }
        final StringBuilder builder = new StringBuilder(player + " blocks with ");
        MagicMessage.addNames(builder, names);
        builder.append('.');
        logBook.add(new MagicMessage(this, player, builder.toString()));
    }

    public void executeEvent(final MagicEvent event,final Object[] choiceResults) {
        if (choiceResults == null) {
            throw new RuntimeException("choiceResults is null");
        }

        logAppendEvent(event,choiceResults);

        // Payed cost
        if (event.getManaChoiceResultIndex() >= 0) {
            payedCost.set(choiceResults[event.getManaChoiceResultIndex()]);
        }

        // Target in cost
        if (event.getTargetChoiceResultIndex() >= 0) {
            payedCost.set(choiceResults[event.getTargetChoiceResultIndex()]);
        }

        activeSource = event.getSource();
        event.executeEvent(this,choiceResults);
        if (hasNextEvent() && getNextEvent().hasChoice()) {
            update();
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

    public boolean advanceToNextEventWithChoice() {
        while (!isFinished()) {
            if (!hasNextEvent()) {
                executePhase();
            } else if (!getNextEvent().hasChoice()) {
                executeNextEvent();
            } else {
                return true;
            }
        }
        return false;
    }

    public List<Object[]> advanceToNextEventWithChoices() {
        while (!isFinished()) {
            if (!hasNextEvent()) {
                executePhase();
            } else if (!getNextEvent().hasChoice()) {
                executeNextEvent();
            } else {
                final MagicEvent event = getNextEvent();
                final List<Object[]> choices = event.getArtificialChoiceResults(this);
                if (choices.size() == 1) {
                    executeNextEvent(choices.get(0));
                } else {
                    return choices;
                }
            }
        }
        return Collections.emptyList();
    }

    public void addNextCostEvent(final MagicEvent event) {
        event.setCost();
        doAction(new AddFirstEventAction(event));
    }

    public void addCostEvent(final MagicEvent event) {
        event.setCost();
        doAction(new AddEventAction(event));
    }

    public void addEvent(final MagicEvent event) {
        doAction(new AddEventAction(event));
    }

    public void addFirstEvent(final MagicEvent event) {
        doAction(new AddFirstEventAction(event));
    }

    public void executeNextEvent(final Object[] choiceResults) {
        doAction(new ExecuteFirstEventAction(choiceResults));
    }

    public void executeNextEvent() {
        doAction(new ExecuteFirstEventAction(MagicEvent.NO_CHOICE_RESULTS));
    }

    public MagicDuel getDuel() {
        return duel;
    }

    public void advanceDuel() {
        duel.advance(losingPlayer != players[0], this);
    }

    public MagicPlayer[] getPlayers() {
        return players;
    }

    /**
     * Gets players ordered as Active Player then Non-Active Player.
     * <p>
     * @see <a href="http://www.slightlymagic.net/forum/viewtopic.php?f=115&p=155684">APNAP forum topic</a>
     */
    public List<MagicPlayer> getAPNAP() {
        return Arrays.asList(turnPlayer, turnPlayer.getOpponent());
    }

    public MagicPlayer getPlayer(final int index) {
        return players[index];
    }

    MagicPlayer getOpponent(final MagicPlayer player) {
        return players[1-player.getIndex()];
    }

    public void setTurnPlayer(final MagicPlayer aTurnPlayer) {
        turnPlayer = aTurnPlayer;
    }

    public MagicPlayer getTurnPlayer() {
        return turnPlayer;
    }

    public MagicPlayer getAttackingPlayer() {
        return turnPlayer;
    }

    public MagicPlayer getDefendingPlayer() {
        return turnPlayer.getOpponent();
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

    public MagicPlayer getWinner() {
        return players[0] == losingPlayer ? players[1] : players[0];
    }

    public MagicSource getActiveSource() {
        return activeSource;
    }

    public boolean hasTurn(final MagicPlayer player) {
        return player == turnPlayer;
    }

    public int getNrOfPermanents(final MagicPermanentState state) {
        return Arrays.stream(players)
            .mapToInt(p -> p.getNrOfPermanents(state))
            .sum();
    }

    public int getNrOfPermanents(final MagicType type) {
        return Arrays.stream(players)
            .mapToInt(p -> p.getNrOfPermanents(type))
            .sum();
    }

    public int getNrOfPermanents(final MagicSubType subType) {
        return Arrays.stream(players)
            .mapToInt(p -> p.getNrOfPermanents(subType))
            .sum();
    }

    public int getNrOfPermanents(final MagicColor color) {
        return Arrays.stream(players)
            .mapToInt(p -> p.getNrOfPermanents(color))
            .sum();
    }

    public int getNrOfPermanents(final MagicTargetFilter<MagicPermanent> filter) {
        return getNrOfPermanents(MagicSource.NONE, filter);
    }

    public int getNrOfPermanents(final MagicSource source, final MagicTargetFilter<MagicPermanent> filter) {
        return Arrays.stream(players)
            .mapToInt(p -> p.getNrOfPermanents(source, filter))
            .sum();
    }

    public boolean canPlaySorcery(final MagicPlayer controller) {
        return phase.getType().isMain() &&
               stack.isEmpty() &&
               turnPlayer == controller;
    }

    public boolean canPlayLand(final MagicPlayer controller) {
        return landsPlayed < maxLands && canPlaySorcery(controller);
    }

    public int getLandsPlayed() {
        return landsPlayed;
    }

    public void incLandsPlayed() {
        landsPlayed++;
    }

    public void resetLandsPlayed() {
        landsPlayed = 0;
    }

    public void setLandsPlayed(final int lp) {
        landsPlayed = lp;
    }

    public void incMaxLands() {
        maxLands++;
    }

    public void resetMaxLands() {
        maxLands = 1;
    }

    public int getSpellsCast() {
        int spellCount = 0;
        for (final MagicPlayer player : players) {
            spellCount += player.getSpellsCast();
        }
        return spellCount;
    }

    public int getSpellsCastLastTurn() {
        int spellCount = 0;
        for (final MagicPlayer player : players) {
            spellCount += player.getSpellsCastLastTurn();
        }
        return spellCount;
    }

    public void incSpellsCast(final MagicPlayer player) {
        player.incSpellsCast();
    }

    public boolean getCreatureDiedThisTurn() {
        return creatureDiedThisTurn;
    }

    public void setCreatureDiedThisTurn(final boolean died) {
        creatureDiedThisTurn = died;
    }

    public MagicStack getStack() {
        return stack;
    }

    public MagicStack getPendingTriggers() {
        return pendingStack;
    }

    public boolean hasItem(final MagicSource source, final String desc) {
        return stack.hasItem(source, desc) || pendingStack.hasItem(source, desc);
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

    public MagicSource createDelayedSource(final MagicCardDefinition cdef, final MagicPlayer controller) {
        return new MagicCard(cdef, controller.map(this), getUniqueId());
    }

    public MagicSource createDelayedSource(final MagicObject obj, final MagicPlayer controller) {
        return new MagicCard(obj.getCardDefinition(), controller.map(this), getUniqueId());
    }

    public MagicPermanent createPermanent(final MagicCard card,final MagicPlayer controller) {
        return new MagicPermanent(getUniqueId(),card,controller);
    }

    public MagicPermanent createPermanent(final MagicCard card, final MagicCardDefinition cardDef, final MagicPlayer controller) {
        return new MagicPermanent(getUniqueId(),card,cardDef,controller);
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

    public void checkStatePutTriggers() {
        while (stateCheckRequired) {
            stateCheckRequired = false;

            // Check if a player has lost
            for (final MagicPlayer player : getAPNAP()) {
                player.generateStateBasedActions();
            }

            // Check permanents' state
            for (final MagicPlayer player : getAPNAP()) {
            for (final MagicPermanent permanent : player.getPermanents()) {
                permanent.generateStateBasedActions();
            }}

            update();
            // some action may set stateCheckRequired to true, if so loop again
        }

        // update log with messages from state-based actions
        logMessages();

        // put pending triggers on stack in APNAP order
        pendingStack.sortAPNAP(getTurnPlayer());
        while (!pendingStack.isEmpty()) {
            doAction(new PutItemOnStackAction(pendingStack.peek()));
            doAction(new DequeueTriggerAction());
        }
    }

    public void checkUniquenessRule(final MagicPermanent permanent) {
        // 704.5k "legend rule"
        if (permanent.hasType(MagicType.Legendary)) {
            final MagicTargetFilter<MagicPermanent> targetFilter=new MagicLegendaryCopiesFilter(permanent.getName());
            final Collection<MagicPermanent> targets=targetFilter.filter(permanent.getController());
            if (targets.size() > 1) {
                addEvent(new MagicUniquenessEvent(permanent, targetFilter));
            }
        }

        // 704.5m "world rule"
        if (permanent.hasType(MagicType.World)) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(MagicTargetFilterFactory.WORLD, permanent);
            final Collection<MagicPermanent> targets = targetFilter.filter(permanent.getController());
            for (final MagicPermanent world : targets) {
                logAppendMessage(
                    world.getController(),
                    MagicMessage.format("%s is put into its owner's graveyard.", world)
                );
                doAction(new RemoveFromPlayAction(
                    world,
                    MagicLocationType.Graveyard
                ));
            }
        }
    }

    public Object[] map(final Object[] data) {
        final int length=data.length;
        final Object[] mappedData=new Object[length];
        for (int index=0;index<length;index++) {
            final Object obj=data[index];
            if (obj != null && obj instanceof MagicMappable) {
                mappedData[index]=((MagicMappable)obj).map(this);
            } else {
                assert obj == null ||
                       obj instanceof Enum ||
                       obj instanceof Number ||
                       obj instanceof String :
                       obj.getClass().getName() + " not mapped";
                mappedData[index]=obj;
            }
        }
        return mappedData;
    }

    // ***** TARGETTING *****

    public boolean hasLegalTargets(final MagicPlayer player, final MagicSource source, final MagicTargetChoice targetChoice, final boolean hints) {

        if (targetChoice == MagicTargetChoice.NONE) {
            return true;
        }

        final Collection<? extends MagicTarget> targets = targetChoice.getTargetFilter().filter(
            source,
            player,
            targetChoice.getTargetHint(hints)
        );

        if (!targetChoice.isTargeted()) {
            return !targets.isEmpty();
        }

        for (final MagicTarget target : targets) {
            if (target.isValidTarget(source)) {
                return true;
            }
        }

        return false;
    }

    public List<MagicTarget> getLegalTargets(final MagicPlayer player, final MagicSource source, final MagicTargetChoice targetChoice, final MagicTargetHint targetHint) {

        final Collection<? extends MagicTarget> targets = targetChoice.getTargetFilter().filter(
            source,
            player,
            targetHint
        );

        final List<MagicTarget> options;
        if (targetChoice.isTargeted()) {
            options= new ArrayList<>();
            for (final MagicTarget target : targets) {
                if (target.isValidTarget(source)) {
                    options.add(target);
                }
            }
        } else {
            options= new ArrayList<>(targets);
        }

        if (options.isEmpty()) {
            // Try again without using hints
            if (targetHint != MagicTargetHint.None) {
                return getLegalTargets(player, source, targetChoice, MagicTargetHint.None);
            // Add none when there are no legal targets. Only for triggers.
            } else {
                options.add(MagicTargetNone.getInstance());
            }
        }
        return options;
    }

    public <T extends MagicTarget> boolean isLegalTarget(final MagicPlayer player, final MagicSource source, final MagicTargetChoice targetChoice, final T target) {

        @SuppressWarnings("unchecked")
        MagicTargetFilter<T> targetFilter = (MagicTargetFilter<T>)targetChoice.getTargetFilter();

        if (target==null ||
            target==MagicTargetNone.getInstance() ||
            !targetFilter.accept(source,player,target)) {
            return false;
        }

        if (target.isLegalTarget(player, targetFilter)) {
            return !targetChoice.isTargeted() || target.isValidTarget(source);
        }

        return false;
    }

    // ***** STATICS *****

    public void addStatics(final MagicPermanent permanent) {
        for (final MagicStatic mstatic : permanent.getStatics()) {
            addStatic(permanent, mstatic);
        }
    }

    public void addStatics(final MagicPermanent permanent, final Collection<MagicStatic> mstatics) {
        for (final MagicStatic mstatic : mstatics) {
            addStatic(permanent, mstatic);
        }
    }

    public Collection<MagicPermanentStatic> removeSelfStatics(final MagicPermanent permanent) {
        return statics.remove(permanent, permanent.getStatics());
    }

    public Collection<MagicPermanentStatic> removeAllStatics(final MagicPermanent permanent) {
        return statics.remove(permanent);
    }

    public MagicPermanentStatic addStatic(final MagicPermanent permanent, final MagicStatic mstatic) {
        return addStatic(new MagicPermanentStatic(getUniqueId(),permanent,mstatic));
    }

    public MagicPermanentStatic addStatic(final MagicPermanentStatic permanentStatic) {
        statics.add(permanentStatic);
        return permanentStatic;
    }

    public void addStatics(final Collection<MagicPermanentStatic> aStatics) {
        for (final MagicPermanentStatic mpstatic : aStatics) {
            addStatic(mpstatic);
        }
    }

    public Collection<MagicPermanentStatic> getStatics(final MagicLayer layer) {
        return statics.get(layer);
    }

    public Collection<MagicPermanentStatic> removeTurnStatics() {
        return statics.removeTurn();
    }

    public void removeStatic(final MagicPermanent permanent,final MagicStatic mstatic) {
        statics.remove(permanent, mstatic);
    }

    public void removeStatics(final MagicPermanent permanent,final Collection<MagicStatic> mstatics) {
        statics.remove(permanent, mstatics);
    }


    // ***** TRIGGERS *****

    /** Executes triggers immediately when they have no choices, otherwise ignore them. */
    public void setImmediate(final boolean aImmediate) {
        immediate = aImmediate;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public MagicPermanentTrigger addTrigger(final MagicPermanentTrigger permanentTrigger) {
        additionalTriggers.add(permanentTrigger);
        return permanentTrigger;
    }

    public void addTriggers(final List<MagicPermanentTrigger> triggersList) {
        for (final MagicPermanentTrigger permanentTrigger : triggersList) {
            addTrigger(permanentTrigger);
        }
    }

    public MagicPermanentTrigger addTrigger(final MagicPermanent permanent, final MagicTrigger<?> trigger) {
        return addTrigger(new MagicPermanentTrigger(getUniqueId(),permanent,trigger, false));
    }

    public MagicPermanentTrigger addTurnTrigger(final MagicPermanent permanent, final MagicTrigger<?> trigger) {
        return addTrigger(new MagicPermanentTrigger(getUniqueId(),permanent,trigger, true));
    }

    public void removeTrigger(final MagicPermanentTrigger permanentTrigger) {
        additionalTriggers.remove(permanentTrigger);
    }

    public MagicPermanentTrigger removeTrigger(final MagicPermanent permanent, MagicTrigger<?> trigger) {
        return additionalTriggers.remove(permanent, trigger);
    }

    public Collection<MagicPermanentTrigger> removeTriggers(final MagicPermanent permanent) {
        return additionalTriggers.remove(permanent);
    }

    public List<MagicPermanentTrigger> removeTurnTriggers() {
        return additionalTriggers.removeTurn();
    }

    public <T> void executeTrigger(final MagicTrigger<T> trigger, final MagicPermanent permanent, final MagicSource source, final T data) {

        if (!trigger.accept(permanent, data)) {
            return;
        }

        final MagicEvent event=trigger.executeTrigger(this,permanent,data);

        if (!event.isValid()) {
            return;
        }

        if (immediate) {
            if (event.hasChoice()) {
                // ignore
            } else if (trigger.usesStack()) {
                doAction(new EnqueueTriggerAction(event));
            } else {
                executeEvent(event, MagicEvent.NO_CHOICE_RESULTS);
            }
        } else if (trigger.usesStack()) {
            doAction(new EnqueueTriggerAction(event));
        } else {
            addEvent(event);
        }
    }

    public <T> void executeTrigger(final MagicTriggerType type,final T data) {
        final Collection<MagicPermanentTrigger> typeTriggers=triggers.get(type);
        if (typeTriggers.isEmpty()) {
            return;
        }

        final Collection<MagicPermanentTrigger> copiedTriggers= new ArrayList<>(typeTriggers);
        for (final MagicPermanentTrigger permanentTrigger : copiedTriggers) {
            final MagicPermanent permanent = permanentTrigger.getPermanent();
            @SuppressWarnings("unchecked")
            final MagicTrigger<T> trigger = (MagicTrigger<T>)permanentTrigger.getTrigger();
            executeTrigger(trigger,permanent,permanent,data);
        }
    }

    public void setConceded(final boolean b) {
        isConceded = true;
    }
    public boolean isConceded() {
        return isConceded;
    }

    public void playSound(MagicSound aSound) {
        if (isReal()) {
            aSound.play();
        }
    }

    public long getStartTimeMilli() {
        return startTimeMilli;
    }
}
