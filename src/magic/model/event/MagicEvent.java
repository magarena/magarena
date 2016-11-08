package magic.model.event;

import java.util.List;
import java.util.Collections;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicColor;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicObject;
import magic.model.MagicObjectImpl;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
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
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;
import magic.model.target.MagicTargetPicker;
import magic.exception.GameException;

public class MagicEvent implements MagicCopyable {

    public static final Object[] NO_CHOICE_RESULTS = new Object[0];
    public static final MagicCopyable NO_REF = new MagicInteger(-1);

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

    public static final MagicEvent NONE = new MagicEvent(MagicSource.NONE, MagicPlayer.NONE, NO_REF, MagicEventAction.NONE, "") {
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
    private MagicTarget chosenTarget;

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final MagicChoice choice,
        final MagicTargetPicker<?> targetPicker,
        final MagicCopyable ref,
        final MagicEventAction action,
        final String description
    ) {
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
        final String description
    ) {
        this(source,player,choice,targetPicker,new MagicInteger(ref),action,description);
    }


    public MagicEvent(
        final MagicSource source,
        final MagicChoice choice,
        final MagicTargetPicker<?> targetPicker,
        final MagicCopyable ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),choice,targetPicker,ref,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicChoice choice,
        final MagicTargetPicker<?> targetPicker,
        final int ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),choice,targetPicker,new MagicInteger(ref),action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final MagicChoice choice,
        final MagicTargetPicker<?> targetPicker,
        final MagicEventAction action,
        final String description
    ) {
        this(source,player,choice,targetPicker,NO_REF,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicChoice choice,
        final MagicTargetPicker<?> targetPicker,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),choice,targetPicker,NO_REF,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final MagicChoice choice,
        final MagicCopyable ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,player,choice,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final MagicChoice choice,
        final int ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,player,choice,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }


    public MagicEvent(
        final MagicSource source,
        final MagicChoice choice,
        final MagicCopyable ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),choice,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicChoice choice,
        final int ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),choice,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final MagicChoice choice,
        final MagicEventAction action,
        final String description
    ) {
        this(source,player,choice,MagicDefaultTargetPicker.create(),NO_REF,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicChoice choice,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),choice,MagicDefaultTargetPicker.create(),NO_REF,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final MagicCopyable ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,player,MagicChoice.NONE,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final int ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,player,MagicChoice.NONE,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicCopyable ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),MagicChoice.NONE,MagicDefaultTargetPicker.create(),ref,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final int ref,
        final MagicEventAction action,
        final String description
    ) {
        this(source,source.getController(),MagicChoice.NONE,MagicDefaultTargetPicker.create(),new MagicInteger(ref),action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicPlayer player,
        final MagicEventAction action,
        final String description
    ) {
        this(source,player,MagicChoice.NONE,MagicDefaultTargetPicker.create(),NO_REF,action,description);
    }

    public MagicEvent(
        final MagicSource source,
        final MagicEventAction action,
        final String description
    ) {
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

    public final MagicCopyable getRef() {
        return ref;
    }

    public final MagicObject getRefObject() {
        return (MagicObject)ref;
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

    public final MagicCard getSourceCard() {
        return source.isPermanent() ? getPermanent().getCard() : getCard();
    }

    public final MagicCardOnStack getCardOnStack() {
        return (MagicCardOnStack)source;
    }

    public final MagicItemOnStack getItemOnStack() {
        return (MagicItemOnStack)source;
    }

    public final int getX() {
        if (ref instanceof MagicPayedCost) {
            return getRefPayedCost().getX();
        } else {
            // occurs when event is MagicStackGetChoicesEvent
            return getRefItemOnStack().getEvent().getX();
        }
    }

    public final boolean hasChoice() {
        return choice.isValid();
    }

    public final MagicChoice getChoice() {
        return choice;
    }

    public final MagicTargetPicker<?> getTargetPicker() {
        return targetPicker;
    }

    public final List<Object[]> getArtificialChoiceResults(final MagicGame game) {
        final long start = System.currentTimeMillis();
        final List<Object[]> choices = choice.getArtificialChoiceResults(game,this);
        final long time = System.currentTimeMillis() - start;
        if (time > 1000) {
            System.err.println("WARNING. ACR:  " + choice.getDescription() + description + " time: " + time);
            /*
            if (getClass().desiredAssertionStatus()) {
                throw new GameException("ACR:  " + choice.getDescription() + description + " time: " + time, game);
            }
            */
        }
        return choices;
    }

    public final Object[] getSimulationChoiceResult(final MagicGame game) {
        final long start = System.currentTimeMillis();
        final Object[] res = choice.getSimulationChoiceResult(game,this);
        final long time = System.currentTimeMillis() - start;
        if (time > 1000) {
            System.err.println("WARNING. RCR:  " + choice.getDescription() + description + " time: " + time);
            /*
            if (getClass().desiredAssertionStatus()) {
                throw new GameException("RCR:  " + choice.getDescription() + description + " time: " + time, game);
            }
            */
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
        for (final Object obj : chosen) {
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

    public MagicPlayer getChosenPlayer() {
        for (Object obj : chosen) {
            if (obj instanceof  MagicPlayer) {
                return (MagicPlayer)obj;
            }
        }
        throw new RuntimeException("Unable to find player");
    }

    private MagicTarget getLegalTarget(final MagicGame game) {
        for (final Object obj : chosen) {
            if (obj instanceof MagicTarget) {
                return getLegalTarget(game, (MagicTarget)obj);
            }
        }
        return null;
    }

    private final MagicTarget getLegalTarget(final MagicGame game, final MagicTarget target) {
        final MagicTargetChoice targetChoice = getTargetChoice();
        if (game.isLegalTarget(player,source,targetChoice,target)) {
            return target;
        } else {
            return MagicTargetNone.getInstance();
        }
    }

    public final boolean hasLegalTarget() {
        return chosenTarget != MagicTargetNone.getInstance();
    }

    public final boolean isValid(final MagicGame game, final Object[] choiceResults) {
        chosen = choiceResults;
        chosenTarget = getLegalTarget(game);
        final boolean countered =
            getTargetChoice().isValid() &&
            getTargetChoice().isTargeted() &&
            chosenTarget == MagicTargetNone.getInstance();
        chosen = null;
        chosenTarget = null;
        return countered == false;
    }

    public final boolean processTarget(final MagicGame game, final MagicTargetAction effect) {
        final MagicTarget target = chosenTarget;
        if (target != MagicTargetNone.getInstance()) {
            effect.doAction(target);
            return true;
        } else {
            return false;
        }
    }

    public final List<MagicTarget> listTarget() {
        final MagicTarget target = chosenTarget;
        return target != MagicTargetNone.getInstance() ? Collections.singletonList(target) : Collections.emptyList();
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
        final MagicTarget target = chosenTarget;
        if (target.isPermanent()) {
            effect.doAction((MagicPermanent)target);
            return true;
        } else {
            return false;
        }
    }

    public final List<MagicPermanent> listTargetPermanent() {
        final MagicTarget target = chosenTarget;
        return target.isPermanent() ? Collections.singletonList((MagicPermanent)target) : Collections.emptyList();
    }

    public final List<MagicPlayer> listTargetController() {
        final MagicTarget target = chosenTarget;
        return target != MagicTargetNone.getInstance() ? Collections.singletonList(target.getController()) : Collections.emptyList();
    }

    public final boolean processTargetCardOnStack(final MagicGame game, final MagicCardOnStackAction effect) {
        final MagicTarget target = chosenTarget;
        if (target.isSpell()) {
            effect.doAction((MagicCardOnStack)target);
            return true;
        } else {
            return false;
        }
    }

    public final boolean processTargetItemOnStack(final MagicGame game, final MagicItemOnStackAction effect) {
        final MagicTarget target = chosenTarget;
        if (target instanceof MagicItemOnStack) {
            effect.doAction((MagicItemOnStack)target);
            return true;
        } else {
            return false;
        }
    }

    public final List<MagicItemOnStack> listTargetItem() {
        final MagicTarget target = chosenTarget;
        return target instanceof MagicItemOnStack ? Collections.singletonList((MagicItemOnStack)target) : Collections.emptyList();
    }

    public final void processChosenCards(final MagicGame game, final MagicCardAction effect) {
        for (final MagicCard card : getCardChoice()) {
            effect.doAction(card);
        }
    }

    public final boolean processTargetCard(final MagicGame game, final MagicCardAction effect) {
        final MagicTarget target = chosenTarget;
        if (target.isSpell()) {
            effect.doAction((MagicCard)target);
            return true;
        } else {
            return false;
        }
    }

    public final List<MagicCard> listTargetCard() {
        final MagicTarget target = chosenTarget;
        return target.isSpell() ? Collections.singletonList((MagicCard)target) : Collections.emptyList();
    }

    public final boolean processTargetPlayer(final MagicGame game, final MagicPlayerAction effect) {
        final MagicTarget target = chosenTarget;
        if (target.isPlayer()) {
            effect.doAction((MagicPlayer)target);
            return true;
        } else {
            return false;
        }
    }

    public final List<MagicPlayer> listTargetPlayer() {
        final MagicTarget target = chosenTarget;
        return target.isPlayer() ? Collections.singletonList((MagicPlayer)target) : Collections.emptyList();
    }

    private static final void payManaCost(final MagicGame game, final MagicPlayer player, final MagicPayManaCostResult result) {
        result.doAction(game,player);
        // Let each payed mana cost influence score.
        game.changeScore(ArtificialScoringSystem.getManaScore(result.getConverted()));
    }

    public final void payManaCost(final MagicGame game) {
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
        chosenTarget = getLegalTarget(game);
        payManaCost(game);
        action.executeEvent(game,this);
        chosen = null;
        chosenTarget = null;
    }

    public final void executeAllEvents(final MagicGame game, final MagicSourceEvent... sourceEvents) {
        for (int i = 0; i < sourceEvents.length; i++) {
            sourceEvents[i].getAction().executeEvent(game, this);
        }
    }

    public final void executeModalEvent(final MagicGame game, final MagicSourceEvent... sourceEvents) {
        for (int i = 0; i < sourceEvents.length; i++) {
            if (isMode(i+1)) {
                sourceEvents[i].getAction().executeEvent(game, this);
                break;
            }
        }
    }

    public boolean isSatisfied() {
        return choice.hasOptions(player.getGame(), player, source, false);
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

    public MagicEventAction getEventAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }
}
