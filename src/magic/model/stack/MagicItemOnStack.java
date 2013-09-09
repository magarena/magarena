package magic.model.stack;

import magic.model.MagicCardDefinition;
import magic.model.MagicCopyMap;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicColor;
import magic.model.MagicAbility;
import magic.model.MagicType;
import magic.model.MagicSubType;
import magic.model.MagicPayedCost;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.event.MagicCardEvent;
import magic.model.event.MagicCardEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import javax.swing.ImageIcon;

public abstract class MagicItemOnStack implements MagicTarget, MagicMappable<MagicItemOnStack> {

    private final MagicSource source;
    private final MagicPlayer controller;
    private final MagicEvent event;
    private final MagicActivation<?> activation; //may be null
    private final long id;

    private Object[] choiceResults=MagicEvent.NO_CHOICE_RESULTS;

    MagicItemOnStack(final MagicSource aSource, final MagicPlayer aController, final MagicEvent aEvent, final MagicActivation<?> act) {
        source = aSource;
        controller = aController;
        event = aEvent;
        activation = act;
        id = aSource.getGame().getUniqueId();
    }

    MagicItemOnStack(final MagicSource aSource, final MagicPlayer aController, final MagicEvent aEvent) {
        this(aSource, aController, aEvent, null);
    }

    //hack for MagicCardOnStack
    MagicItemOnStack(final MagicSource aSource, final MagicPlayer aController, final MagicCardEvent cardEvent, final MagicPayedCost payedCost) {
        source = aSource;
        controller = aController;
        event = cardEvent.getEvent((MagicCardOnStack)this, payedCost);
        activation = null;
        id = aSource.getGame().getUniqueId();
    }

    MagicItemOnStack(final MagicCopyMap copyMap, final MagicItemOnStack sourceItem) {
        source = copyMap.copy(sourceItem.source);
        controller = copyMap.copy(sourceItem.controller);
        activation = sourceItem.activation;
        event = copyMap.copy(sourceItem.event);
        choiceResults = copyMap.copyObjects(sourceItem.choiceResults,Object.class);
        id=sourceItem.id;
    }

    @Override
    public MagicItemOnStack map(final MagicGame game) {
        return game.getStack().getItemOnStack(id);
    }

    public MagicSource getSource() {
        return source;
    }

    @Override
    public MagicPlayer getController() {
        return controller;
    }
    
    @Override
    public MagicPlayer getOpponent() {
        return getController().getOpponent();
    }

    @Override
    public boolean isFriend(final MagicObject other) {
        return getController() == other.getController();
    }

    @Override
    public boolean isEnemy(final MagicObject other) {
        return getOpponent() == other.getController();
    }

    @Override
    public MagicCardDefinition getCardDefinition() {
        return getSource().getCardDefinition();
    }
    
    public int getConvertedCost() {
        return 0;
    }

    MagicActivation<?> getActivation() {
        return activation;
    }

    public MagicEvent getEvent() {
        return event;
    }

    public boolean hasChoice() {
        return event.hasChoice();
    }

    public void setChoiceResults(final Object[] choiceResults) {
        this.choiceResults=choiceResults;
    }

    public Object[] getChoiceResults() {
        return choiceResults;
    }

    public boolean containsInChoiceResults(final MagicObject obj) {
        if (choiceResults != null) {
            for (final Object choiceResult : choiceResults) {
                if (choiceResult == obj) {
                    return true;
                }
            }
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public void resolve(final MagicGame game) {
        game.executeEvent(event,choiceResults);
    }

    @Override
    public String getName() {
        return source.getName();
    }

    public String getDescription() {
        return event.getDescription(choiceResults);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isCreature() {
        return false;
    }

    @Override
    public boolean isPlaneswalker() {
        return false;
    }

    @Override
    public int getPreventDamage() {
        return 0;
    }

    @Override
    public void setPreventDamage(final int amount) {

    }

    @Override
    public boolean isValidTarget(final MagicSource aSource) {
        return aSource != this.source && aSource != this;
    }

    @Override
    public boolean hasColor(final MagicColor color) {
        return source.hasColor(color);
    }

    @Override
    public boolean hasAbility(final MagicAbility ability) {
        return source.hasAbility(ability);
    }

    @Override
    public boolean hasType(final MagicType type) {
        return source.hasType(type);
    }

    @Override
    public boolean hasSubType(final MagicSubType subType) {
        return source.hasSubType(subType);
    }

    @Override
    public boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter) {
        return source.getGame().getStack().contains(this);
    }

    private long getStateId(final Object[] arr, final int idx) {
        if (idx >= arr.length || arr[idx] == null) {
            return -1L;
        }
        final Object obj = arr[idx];
        if (obj instanceof MagicPlayer) {
            return ((MagicPlayer)obj).getId();
        } else if (obj instanceof MagicObject) {
            return ((MagicObject)obj).getStateId();
        } else if (obj instanceof MagicMappable) {
            return ((MagicMappable)obj).getId();
        } else {
            return obj.hashCode();
        }
    }

    public long getStateId() {
        return magic.MurmurHash3.hash(new long[] {
            source != null ?  source.getStateId() : -1L,
            controller != null ? controller.getId() : -1L,
            activation != null ? activation.hashCode() : -1L,
            event != null ? event.getStateId() : -1L,
            getStateId(choiceResults, 0),
            getStateId(choiceResults, 1),
            getStateId(choiceResults, 2),
        });
    }
   
    public abstract boolean isSpell();

    public boolean isSpell(MagicType type) {
        return isSpell() && getCardDefinition().hasType(type);
    }
    
    public boolean isInstantOrSorcery() {
        return getCardDefinition().isSpell();
    }

    public abstract boolean canBeCountered();

    public abstract ImageIcon getIcon();
}
