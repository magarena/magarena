package magic.model.event;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicColor;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicObjectImpl;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.ARG;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicItemOnStackAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicDeclareAttackersResult;
import magic.model.choice.MagicDeclareBlockersResult;
import magic.model.choice.MagicExcludeResult;
import magic.model.choice.MagicPayManaCostResult;
import magic.model.choice.MagicPlayChoiceResult;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;
import magic.model.target.MagicTargetPicker;

import java.util.List;

public class MagicEvent implements MagicCopyable {

    public static final Object[] NO_CHOICE_RESULTS = new Object[0];
    public static final MagicSource NO_SOURCE = MagicCard.NONE;
    public static final MagicEvent[] NO_EVENTS = new MagicEvent[0];
    public static final MagicCopyable NO_REF = new MagicInteger(-1);
    public static final MagicEventAction NO_ACTION = new MagicEventAction() {
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            //do nothing
        }
    };

    static class MagicInteger implements MagicCopyable {
        public final int value;

        public MagicInteger(final int v) {
            value = v;
        }

        @Override
        public MagicCopyable copy(final MagicCopyMap copyMap) {
            return this;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    public static final MagicEvent NONE = new MagicEvent(NO_SOURCE, MagicPlayer.NONE, NO_REF, NO_ACTION, "") {
        @Override
        public boolean isValid() {
            return false;
        }
        @Override
        public MagicEvent copy(final MagicCopyMap copyMap) {
            return this;
        }
        @Override
        public final MagicTargetChoice getTargetChoice() {
            throw new RuntimeException("getTargetChoice called on MagicEvent.NONE");
        }
    };

    private final MagicSource source;
    private final MagicPlayer player;
    private final MagicChoice choice;
    private final MagicTargetPicker<?> targetPicker;
    private final MagicEventAction action;
    private final String description;
    private final MagicCopyable ref;
    private Object[] chosen;

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final MagicTargetPicker<?> targetPicker,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
        assert source != null : "source is null";
        assert player != null : "player is null";
        assert choice != null : "choice is null";
        assert targetPicker != null : "targetPicker is null";
        assert ref          != null : "ref is null";
        assert action       != null : "action is null";
        assert description  != null : "description is null";
        this.source=source;
        this.player=player;
        this.choice=choice;
        this.targetPicker=targetPicker;
        this.ref=ref;
        this.action=action;
        this.description=MagicMessage.replaceName(description,source,player,ref);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final MagicTargetPicker<?> targetPicker,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,player,choice,targetPicker,new MagicInteger(ref),action,description);
    }


    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicTargetPicker<?> targetPicker,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,targetPicker,ref,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicTargetPicker<?> targetPicker,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,targetPicker,new MagicInteger(ref),action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final MagicTargetPicker<?> targetPicker,
            final MagicEventAction action,
            final String description) {
        this(source,player,choice,targetPicker,NO_REF,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicTargetPicker<?> targetPicker,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,targetPicker,NO_REF,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
        this(source,player,choice,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,player,choice,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }


    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final MagicEventAction action,
            final String description) {
        this(source,player,choice,MagicDefaultTargetPicker.create(),NO_REF,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,MagicDefaultTargetPicker.create(),NO_REF,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
        this(source,player,MagicChoice.NONE,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,player,MagicChoice.NONE,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),MagicChoice.NONE,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),MagicChoice.NONE,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicEventAction action,
            final String description) {
        this(source,player,MagicChoice.NONE,MagicDefaultTargetPicker.create(),NO_REF,action,description);
    }

    public MagicEvent(
            final MagicSource source,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),MagicChoice.NONE,MagicDefaultTargetPicker.create(),NO_REF,action,description);
    }

    private MagicEvent(final MagicCopyMap copyMap, final MagicEvent sourceEvent) {
        copyMap.put(sourceEvent,this);

        source = copyMap.copy(sourceEvent.source);
        player = copyMap.copy(sourceEvent.player);
        choice = sourceEvent.choice;
        targetPicker = sourceEvent.targetPicker;
        ref = copyMap.copy(sourceEvent.ref);
        action = sourceEvent.action;
        description = sourceEvent.description;
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicEvent(copyMap, this);
    }

    public boolean isValid() {
        return true;
    }
    
    public final MagicSource getSource() {
        return source;
    }

    public final MagicPlayer getPlayer() {
        return player;
    }

    public final boolean hasRef() {
        return ref != NO_REF;
    }
    
    public final MagicPermanent getPermanent() {
        return (MagicPermanent)source;
    }

    public final MagicPermanent getRefPermanent() {
        return (MagicPermanent)ref;
    }

    public final MagicPermanentList getRefPermanentList() {
        return (MagicPermanentList)ref;
    }
    
    public final MagicCardList getRefCardList() {
        return (MagicCardList)ref;
    }

    public final MagicPlayer getRefPlayer() {
        return (MagicPlayer)ref;
    }

    public final MagicItemOnStack getRefItemOnStack() {
        return (MagicItemOnStack)ref;
    }

    public final MagicCardOnStack getRefCardOnStack() {
        return (MagicCardOnStack)ref;
    }

    public final MagicCard getRefCard() {
        return (MagicCard)ref;
    }
    
    public final MagicSource getRefSource() {
        return (MagicSource)ref;
    }
    
    public final MagicTarget getRefTarget() {
        return (MagicTarget)ref;
    }

    public final MagicPermanentActivation getRefPermanentActivation() {
        return (MagicPermanentActivation)ref;
    }

    public final int getRefInt() {
        return ((MagicInteger)ref).value;
    }
    
    public final MagicPayedCost getRefPayedCost() {
        return (MagicPayedCost)ref;
    }

    public final MagicCard getCard() {
        return (MagicCard)source;
    }

    public final MagicCardOnStack getCardOnStack() {
        return (MagicCardOnStack)source;
    }

    public final boolean hasChoice() {
        return choice.isValid();
    }

    public final MagicChoice getChoice() {
        return choice;
    }
    
    public final boolean hasOptions() {
        return choice.hasOptions(player.getGame(), player, source, false);
    }

    public final MagicTargetPicker<?> getTargetPicker() {
        return targetPicker;
    }

    public final List<Object[]> getArtificialChoiceResults(final MagicGame game) {
        final long start = System.currentTimeMillis();
        final List<Object[]> choices = choice.getArtificialChoiceResults(game,this,player,source);
        final long time = System.currentTimeMillis() - start;
        if (time > 1000) {
            System.err.println("WARNING. ACR:  " + choice.getDescription() + description + " time: " + time);
        }
        return choices;
    }

    public final Object[] getSimulationChoiceResult(final MagicGame game) {
        final long start = System.currentTimeMillis();
        final Object[] res = choice.getSimulationChoiceResult(game,this,player,source);
        final long time = System.currentTimeMillis() - start;
        if (time > 1000) {
            System.err.println("WARNING. RCR:  " + choice.getDescription() + description + " time: " + time);
        }
        return res;
    }

    public MagicTargetChoice getTargetChoice() {
        return chosen != null ? choice.getTargetChoice(chosen) : choice.getTargetChoice();
    }

    public void clearTargetChoice(Object[] choiceResults) {
        choiceResults[getTargetChoiceResultIndex()] = null;
    }

    public void setTargetChoice(Object[] choiceResults) {
        choiceResults[getTargetChoiceResultIndex()] = getTarget();
    }

    public final int getTargetChoiceResultIndex() {
        return choice.getTargetChoiceResultIndex();
    }

    public final int getManaChoiceResultIndex() {
        return choice.getManaChoiceResultIndex();
    }

    public final String getDescription(final Object[] choiceResults) {
        return MagicMessage.replaceChoices(description, choiceResults);
    }

    public final String getChoiceDescription() {
        final String tDescription=getDescription(MagicEvent.NO_CHOICE_RESULTS);
        if (tDescription.length() > 0) {
            return tDescription;
        }
        return hasChoice()?choice.getDescription():"";
    }

    public final MagicTarget getTarget() {
        for (Object obj : chosen) {
            if (obj instanceof MagicTarget) {
                return (MagicTarget)obj;
            }
        }
        throw new RuntimeException("Unable to find target");
    }

    public boolean isMode(final int mode) {
        return (Integer)chosen[0] == mode;
    }

    public boolean isYes() {
        return MagicChoice.isYesChoice(chosen[0]);
    }

    public boolean isNo() {
        return MagicChoice.isNoChoice(chosen[0]);
    }

    public boolean isBuyback() {
        return getCardOnStack().isKicked();
    }

    public boolean isKicked() {
        return getCardOnStack().isKicked();
    }

    public int getKickerFromChosen() {
        return (Integer)chosen[1];
    }

    public MagicDeclareBlockersResult getBlockers() {
        return (MagicDeclareBlockersResult)chosen[0];
    }

    public MagicDeclareAttackersResult getAttackers() {
        return (MagicDeclareAttackersResult)chosen[0];
    }

    public MagicExcludeResult getExclude() {
        return (MagicExcludeResult)chosen[0];
    }

    public MagicPlayChoiceResult getPlayChoice() {
        return (MagicPlayChoiceResult)chosen[0];
    }

    public MagicCardChoiceResult getCardChoice() {
        return (MagicCardChoiceResult)chosen[0];
    }

    public MagicColor getChosenColor() {
        for (Object obj : chosen) {
            if (obj instanceof MagicColor) {
                return (MagicColor)obj;
            }
        }
        throw new RuntimeException("Unable to find chosen color");
    }
    
    public MagicSubType getChosenSubType() {
        for (Object obj : chosen) {
            if (obj instanceof MagicSubType) {
                return (MagicSubType)obj;
            }
        }
        throw new RuntimeException("Unable to find chosen subType");
    }

    public MagicPayManaCostResult getPaidMana() {
        for (Object obj : chosen) {
            if (obj instanceof MagicPayManaCostResult) {
                return (MagicPayManaCostResult)obj;
            }
        }
        throw new RuntimeException("Unable to find paid mana");
    }

    public Object[] getChosen() {
        return chosen;
    }

    private final MagicTarget getLegalTarget(final MagicGame game) {
        final MagicTargetChoice targetChoice = getTargetChoice();
        final MagicTarget target = getTarget();
        if (game.isLegalTarget(player,source,targetChoice,target)) {
            return target;
        } else {
            return MagicTargetNone.getInstance();
        }
    }

    public final boolean hasLegalTarget(final MagicGame game) {
        return getLegalTarget(game) != MagicTargetNone.getInstance();
    }

    public final boolean processTarget(final MagicGame game, final MagicTargetAction effect) {
        final MagicTarget target = getLegalTarget(game);
        if (target != MagicTargetNone.getInstance()) {
            effect.doAction(target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processPermanent(final MagicGame game, final MagicPermanentAction effect) {
        final MagicPermanent target = getPermanent();
        if (target.isValid()) {
            effect.doAction(target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processRefPermanent(final MagicGame game, final MagicPermanentAction effect) {
        final MagicPermanent target = getRefPermanent();
        if (target.isValid()) {
            effect.doAction(target);
            return true;
        } else {
            return false;
        }
    }

    public final boolean processTargetPermanent(final MagicGame game, final MagicPermanentAction effect) {
        final MagicTarget target = getLegalTarget(game);
        if (target.isPermanent()) {
            effect.doAction((MagicPermanent)target);
            return true;
        } else {
            return false;
        }
    }

    public final boolean processTargetCardOnStack(final MagicGame game, final MagicCardOnStackAction effect) {
        final MagicTarget target = getLegalTarget(game);
        if (target.isSpell()) {
            effect.doAction((MagicCardOnStack)target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processTargetItemOnStack(final MagicGame game, final MagicItemOnStackAction effect) {
        final MagicTarget target = getLegalTarget(game);
        if (target instanceof MagicItemOnStack) {
            effect.doAction((MagicItemOnStack)target);
            return true;
        } else {
            return false;
        }
    }
    
    public final void processChosenCards(final MagicGame game, final MagicCardAction effect) {
        for (final MagicCard card : getCardChoice()) {
            effect.doAction(card);
        }
    }

    public final boolean processTargetCard(final MagicGame game, final MagicCardAction effect) {
        final MagicTarget target = getLegalTarget(game);
        if (target.isSpell()) {
            effect.doAction((MagicCard)target);
            return true;
        } else {
            return false;
        }
    }

    public final boolean processTargetPlayer(final MagicGame game, final MagicPlayerAction effect) {
        final MagicTarget target = getLegalTarget(game);
        if (target.isPlayer()) {
            effect.doAction((MagicPlayer)target);
            return true;
        } else {
            return false;
        }
    }

    private static final void payManaCost(
            final MagicGame game,
            final MagicPlayer player,
            final MagicPayManaCostResult result) {
        result.doAction(game,player);
        // Let each payed mana cost influence score.
        game.changeScore(ArtificialScoringSystem.getManaScore(result.getConverted()));
    }

    final void payManaCost(final MagicGame game) {
        final int manaIndex=getManaChoiceResultIndex();
        assert manaIndex < chosen.length :
            this + "\n" +
            "manaIndex " + manaIndex + " from " + choice + " is outside of chosen with size " + chosen.length;
        // Result can be null when paying cost is optional.
        if (manaIndex >= 0 && chosen[manaIndex] != null) {
            assert chosen[manaIndex] instanceof MagicPayManaCostResult :
                this + "\n" +
                "manaIndex " + manaIndex + " from " + choice + " is " + chosen[manaIndex].getClass() + " instead of MagicPayManaCostResult";
            payManaCost(game,player,(MagicPayManaCostResult)chosen[manaIndex]);
        }
    }

    public final void executeEvent(final MagicGame game,final Object[] choiceResults) {
        chosen = choiceResults;
        action.executeEvent(game,this);
        chosen = null;
    }
    
    public final void executeAllEvents(final MagicGame game, final MagicSourceEvent... sourceEvents) {
        for (int i = 0; i < sourceEvents.length; i++) {
            sourceEvents[i].getEvent(getSource()).executeEvent(game, getChosen());
        }
    }

    public final void executeModalEvent(final MagicGame game, final MagicSourceEvent... sourceEvents) {
        for (int i = 0; i < sourceEvents.length; i++) {
            if (isMode(i+1)) {
                sourceEvents[i].getEvent(getSource()).executeEvent(game, getChosen());
                break;
            }
        }
    }

    public MagicCondition[] getConditions() {
        return MagicActivation.NO_COND;
    }
    
    public boolean isSatisfied() {
        for (final MagicCondition condition : getConditions()) {
            if (!condition.accept(source)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "EVENT: " + source + " " + description + " " + (hasChoice() ? choice.getDescription() : "");
    }

    public long getStateId() {
        return magic.model.MurmurHash3.hash(new long[] {
            //don't call getStateId if source is MagicItemOnStack to avoid infinite loop
            (source instanceof MagicItemOnStack) ? -1L : source.getStateId(),
            player.getId(),
            choice.getStateId(),
            targetPicker.hashCode(),
            action.hashCode(),
            description.hashCode(),
            MagicObjectImpl.getStateId(ref),
        });
    }

    public MagicEventAction getMagicEventAction() {
        return action;
    }

    public void onAddEvent(final MagicGame game) {
        //do nothing
    }
}
