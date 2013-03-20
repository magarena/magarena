package magic.model.event;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicMappable;
import magic.model.MagicMessage;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicPayManaCostResult;
import magic.model.choice.MagicTargetChoice;
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
    private static final MagicCopyable NO_REF = new MagicInteger(-1);
    private static final MagicEventAction NO_ACTION = new MagicEventAction() {
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            //do nothing
        }
    };

    static class MagicInteger implements MagicCopyable {
        public final int value;

        public MagicInteger(final int v) {
            value = v;
        }
    
        public MagicCopyable copy(final MagicCopyMap copyMap) {
            return this;
        }

        public int hashCode() {
            return value;
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
    private final MagicTargetPicker targetPicker;
    private final MagicEventAction action;
    private final String description;
    private final MagicCopyable ref;

    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final MagicTargetPicker targetPicker,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
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
            final MagicTargetPicker targetPicker,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,player,choice,targetPicker,new MagicInteger(ref),action,description);
    }
    
    
    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicTargetPicker targetPicker,
            final MagicCopyable ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,targetPicker,ref,action,description);
    }
    
    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicTargetPicker targetPicker,
            final int ref,
            final MagicEventAction action,
            final String description) {
        this(source,source.getController(),choice,targetPicker,new MagicInteger(ref),action,description);
    }
    
    public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
            final MagicTargetPicker targetPicker,
            final MagicEventAction action,
            final String description) {
        this(source,player,choice,targetPicker,NO_REF,action,description);
    }
    
    public MagicEvent(
            final MagicSource source,
            final MagicChoice choice,
            final MagicTargetPicker targetPicker,
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
    
    public final MagicPermanent getPermanent() {
        return (MagicPermanent)source;
    }
    
    public final MagicPermanent getRefPermanent() {
        return (MagicPermanent)ref;
    }
    
    public final MagicPermanentList getRefPermanentList() {
        return (MagicPermanentList)ref;
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
    
    public final MagicPermanentActivation getRefPermanentActivation() {
        return (MagicPermanentActivation)ref;
    }
    
    public final int getRefInt() {
        return ((MagicInteger)ref).value;
    }
    
    public final MagicCard getCard() {
        return (MagicCard)source;
    }
    
    public final MagicCardOnStack getCardOnStack() {
        return (MagicCardOnStack)source;
    }
    
    public final MagicPlayer getPlayer() {
        return player;
    }
        
    public final boolean hasChoice() {
        return choice.isValid();
    }
    
    public final MagicChoice getChoice() {
        return choice;
    }
    
    public final MagicTargetPicker getTargetPicker() {
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
        return choice.getTargetChoice();
    }
    
    private final int getManaChoiceResultIndex() {
        return choice.getManaChoiceResultIndex();
    }

    public final String getDescription(final Object[] choiceResults) {
        return MagicMessage.replaceChoices(description,choiceResults);
    }

    public final String getChoiceDescription() {
        final String tDescription=getDescription(MagicEvent.NO_CHOICE_RESULTS);
        if (tDescription.length() > 0) {
            return tDescription;
        }
        return hasChoice()?choice.getDescription():"";
    }
        
    private final MagicTarget findTarget(final Object[] choiceResults) {
        for (Object obj : choiceResults) {
            if (obj instanceof MagicTarget) {
                return (MagicTarget)obj;
            }
        }
        throw new RuntimeException("Unable to find target");
    }
    
    private final MagicTarget getTarget(final MagicGame game,final Object[] choiceResults) {
        final MagicTargetChoice targetChoice = getTargetChoice();
        final MagicTarget target = findTarget(choiceResults);
        if (game.isLegalTarget(player,source,targetChoice,target)) {
            return target;
        } else {
            return MagicTargetNone.getInstance();
        }
    }
    
    public final boolean processTarget(
            final MagicGame game,
            final Object[] choiceResults,
            final MagicTargetAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults);
        if (target != MagicTargetNone.getInstance()) {
            effect.doAction(target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processTargetPermanent(
            final MagicGame game,
            final Object[] choiceResults,
            final MagicPermanentAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults);
        if (target.isPermanent()) {
            effect.doAction((MagicPermanent)target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processTargetCardOnStack(
            final MagicGame game,
            final Object[] choiceResults,
            final MagicCardOnStackAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults);
        if (target.isSpell()) {
            effect.doAction((MagicCardOnStack)target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processTargetCard(
            final MagicGame game,
            final Object[] choiceResults,
            final int index,
            final MagicCardAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults);
        if (target.isSpell()) {
            effect.doAction((MagicCard)target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processTargetPlayer(
            final MagicGame game,
            final Object[] choiceResults,
            final MagicPlayerAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults);
        if (target.isPlayer()) {
            effect.doAction((MagicPlayer)target);
            return true;
        } else {
            return false;
        }
    }

    public final boolean processTargetPlayer(
            final MagicGame game,
            final Object[] choiceResults,
            final int index,
            final MagicPlayerAction effect
            ) {
        return processTargetPlayer(game, choiceResults, effect);
    }

    private static final void payManaCost(
            final MagicGame game,
            final MagicPlayer player,
            final MagicPayManaCostResult result) { 
        result.doAction(game,player);
        // Let each payed mana cost influence score.
        game.changeScore(ArtificialScoringSystem.getManaScore(result.getConverted()));
    }
    
    final void payManaCost(final MagicGame game,final MagicPlayer aPlayer,final Object[] choiceResults) {
        final int manaIndex=getManaChoiceResultIndex();
        assert manaIndex < choiceResults.length : 
            this + "\n" +
            "manaIndex " + manaIndex + " from " + choice + " is outside of choiceResults with size " + choiceResults.length;
        // Result can be null when paying cost is optional.
        if (manaIndex >= 0 && choiceResults[manaIndex] != null) {
            assert choiceResults[manaIndex] instanceof MagicPayManaCostResult : 
                this + "\n" +
                "manaIndex " + manaIndex + " from " + choice + " is " + choiceResults[manaIndex].getClass() + " instead of MagicPayManaCostResult";
            payManaCost(game,aPlayer,(MagicPayManaCostResult)choiceResults[manaIndex]);
        }
    }
    
    public final void executeEvent(final MagicGame game,final Object[] choiceResults) {
        action.executeEvent(game,this,choiceResults);
        //move card to move location that is not play 
        if (source instanceof MagicCardOnStack &&
            action instanceof MagicSpellCardEvent &&
            getCardOnStack().getMoveLocation() != MagicLocationType.Play) {
            game.doAction(new MagicMoveCardAction(getCardOnStack()));
        }
    }

    public String toString() {
        return "EVENT: " + player.getIndex() + " " + description + " " + (hasChoice() ? choice.getDescription() : "");
    }

    public long getEventId() {
        return magic.MurmurHash3.hash(new long[] {
            source.getId(),
            player.getIndex(),
            choice.hashCode(),
            action.hashCode(),
            targetPicker.hashCode(),
            ref.hashCode(), 
        });
    }
}
