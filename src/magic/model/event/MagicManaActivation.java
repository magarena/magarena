package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.condition.MagicCondition;

import java.util.List;

public abstract class MagicManaActivation implements MagicChangeCardDefinition {
    
    private final List<MagicManaType> manaTypes;
    private final MagicCondition[] conditions;
    private final int weight;

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
    
    public final boolean canPlay(final MagicGame game,final MagicPermanent perm) {
        for (final MagicCondition condition : conditions) {
            if (!condition.accept(perm)) {
                return false;
            }
        }
        return true;
    }
    
    public abstract MagicEvent[] getCostEvent(final MagicPermanent perm);
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addManaAct(this);
    }
}
