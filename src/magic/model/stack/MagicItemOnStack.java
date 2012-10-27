package magic.model.stack;

import magic.model.MagicCardDefinition;
import magic.model.MagicCopyMap;
import magic.model.MagicGame;
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

public abstract class MagicItemOnStack implements MagicTarget {
    
    private final MagicSource source;
    private final MagicPlayer controller;
    private final MagicEvent event;
    private final MagicActivation activation; //may be null
    private final long id;
    
    private Object[] choiceResults=MagicEvent.NO_CHOICE_RESULTS;
    
    MagicItemOnStack(final MagicSource aSource, final MagicPlayer aController, final MagicEvent aEvent, final MagicActivation act) {
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
    public MagicCardDefinition getCardDefinition() {
        return getSource().getCardDefinition();
    }
    
    MagicActivation getActivation() {
        return activation;
    }
            
    public MagicEvent getEvent() {
        return event;
    }
    
    public boolean hasChoices() {
        return event.hasChoice();
    }

    public void setChoiceResults(final Object[] choiceResults) {
        this.choiceResults=choiceResults;
    }
    
    public Object[] getChoiceResults() {
        return choiceResults;
    }
    
    public boolean containsInChoiceResults(final MagicPermanent permanent) {
        if (choiceResults!=null) {
            for (final Object choiceResult : choiceResults) {
                if (choiceResult==permanent) {
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
    public int getPreventDamage() {
        return 0;
    }
    
    @Override
    public void setPreventDamage(final int amount) {
        
    }

    @Override
    public boolean isValidTarget(final MagicSource aSource) {
        return aSource!=this.source;
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

    long getItemId() {
        final Object[] CR = choiceResults;
        final long[] keys = {
            id,
            source != null ?  source.getId() : -1L,
            controller != null ? controller.getId() : -1L,
            activation != null ? activation.hashCode() : -1L,
            event != null ? event.getEventId() : -1L,
            (CR.length > 0 && CR[0] != null) ?
                ((CR[0] instanceof MagicMappable) ? ((MagicMappable)CR[0]).getId() : CR[0].hashCode()) :
                -1L,
            (CR.length > 1 && CR[1] != null) ?
                ((CR[1] instanceof MagicMappable) ? ((MagicMappable)CR[1]).getId() : CR[1].hashCode()) :
                -1L,
            (CR.length > 2 && CR[2] != null) ?
                ((CR[2] instanceof MagicMappable) ? ((MagicMappable)CR[2]).getId() : CR[2].hashCode()) :
                -1L,
        };
        return magic.MurmurHash3.hash(keys);
    }
    
    public abstract boolean isSpell();

    public boolean isSpell(MagicType type) {
        return isSpell() && getCardDefinition().hasType(type);
    }

    public abstract boolean canBeCountered();

    public abstract ImageIcon getIcon();
}
