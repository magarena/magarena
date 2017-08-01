package magic.model.stack;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCopyMap;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicObject;
import magic.model.MagicObjectImpl;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.MurmurHash3;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetNone;

public abstract class MagicItemOnStack extends MagicObjectImpl implements MagicTarget, MagicMappable<MagicItemOnStack> {

    private final MagicSource source;
    private final MagicPlayer controller;
    private final MagicEvent event;              //may be null
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

    MagicItemOnStack(final MagicSource aSource, final MagicPlayer aController) {
        this(aSource, aController, null, null);
    }

    MagicItemOnStack(final MagicCopyMap copyMap, final MagicItemOnStack sourceItem) {
        id = sourceItem.id;
        activation = sourceItem.activation;

        copyMap.put(sourceItem, this);

        source = copyMap.copy(sourceItem.source);
        controller = copyMap.copy(sourceItem.controller);
        event = copyMap.copy(sourceItem.event);
        choiceResults = copyMap.copyObjects(sourceItem.choiceResults,Object.class);
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

    // only for rendering the card image popup
    final public MagicCardDefinition getRealCardDefinition() {
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
        return getEvent().hasChoice();
    }

    public void setChoiceResults(final Object[] choiceResults) {
        this.choiceResults=choiceResults;
    }

    public Object[] getChoiceResults() {
        return choiceResults;
    }

    public boolean isTarget(final MagicObject obj) {
        return obj == getTarget();
    }

    public MagicTarget getTarget() {
        for (Object obj : choiceResults) {
            if (obj instanceof MagicTarget) {
                return (MagicTarget)obj;
            }
        }
        return MagicTargetNone.getInstance();
    }

    @Override
    public long getId() {
        return id;
    }

    public void resolve(final MagicGame game) {
        final MagicEvent resolveEvent = getEvent();
        if (resolveEvent.isValid(game, choiceResults)) {
            game.executeEvent(resolveEvent, choiceResults);
        }
    }

    @Override
    public String getName() {
        return source.getName();
    }

    public String getDescription() {
        return getEvent().getDescription(choiceResults);
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
    public int getPreventDamage() {
        return 0;
    }

    @Override
    public void setPreventDamage(final int amount) {

    }

    @Override
    public boolean isValidTarget(final MagicSource aSource) {
        if (aSource instanceof MagicItemOnStack) {
            return aSource.getId() > getId();
        } else {
            return aSource != this;
        }
    }

    @Override
    public boolean hasColor(final MagicColor color) {
        return source.hasColor(color);
    }

    @Override
    public int getColorFlags() {
        return source.getColorFlags();
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
    public int getCounters(final MagicCounterType counterType) {
        return 0;
    }

    @Override
    public void changeCounters(final MagicCounterType counterType,final int amount) {
        throw new RuntimeException(counterType + " cannot be modified on item on stack");
    }

    @Override
    public boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter) {
        return source.getGame().getStack().contains(this);
    }

    private long getStateId(final Object[] arr) {
        final long[] keys = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            keys[i] = MagicObjectImpl.getStateId(arr[i]);
        }
        return MurmurHash3.hash(keys);
    }

    @Override
    public long getStateId() {
        return MurmurHash3.hash(new long[] {
            source     != null ? source.getStateId() : -1L,
            controller != null ? controller.getId()  : -1L,
            activation != null ? activation.hashCode()   : -1L,
            getEvent() != null ? getEvent().getStateId() : -1L,
            getStateId(choiceResults)
        });
    }

    @Override
    public abstract boolean isSpell();

    public abstract boolean canBeCountered();

}
