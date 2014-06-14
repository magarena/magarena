package magic.model.event;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayerState;
import magic.model.condition.MagicCondition;

import java.util.List;

public abstract class MagicManaActivation implements MagicChangeCardDefinition {

    private final List<MagicManaType> manaTypes;
    private final MagicCondition[] conditions;
    private final int weight;
    
    public MagicManaActivation(final List<MagicManaType> manaTypes) {
        this(manaTypes, MagicActivation.NO_COND, manaTypes.size() - 1);
    }

    public MagicManaActivation(final List<MagicManaType> manaTypes, final int weight) {
        this(manaTypes, MagicActivation.NO_COND, weight);
    }

    public MagicManaActivation(final List<MagicManaType> manaTypes, final MagicCondition[] conditions, final int weight) {
        this.manaTypes=manaTypes;
        this.conditions=conditions;
        this.weight=weight;
    }

    public final List<MagicManaType> getManaTypes() {
        return manaTypes;
    }

    public final int getWeight() {
        return weight;
    }

    public final boolean canPlay(final MagicGame game,final MagicPermanent source) {
        // Check if source or player has can't activate activated abilities
        if (source.hasAbility(MagicAbility.CantActivateAbilities) ||
            source.getController().hasState(MagicPlayerState.CantActivateAbilities)) {
            return false;
        }
        
        // Check conditions for activation
        for (final MagicCondition condition : conditions) {
            if (!condition.accept(source)) {
                return false;
            }
        }

        // Check able to pay costs
        for (final MagicEvent event : getCostEvent(source)) {
            for (final MagicCondition condition : event.getConditions()) {
                if (!condition.accept(source)) {
                    return false;
                }
            }
        }

        return true;
    }

    public abstract Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source);

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addManaAct(this);
    }
}
