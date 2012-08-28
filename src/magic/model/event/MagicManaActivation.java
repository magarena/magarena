package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicSource;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicCardDefinition;
import magic.model.condition.MagicCondition;

import java.util.List;

public abstract class MagicManaActivation implements MagicChangeCardDefinition {
    
    private final List<MagicManaType> manaTypes;
    private final MagicCondition conditions[];
    private final int weight;

    public MagicManaActivation(final List<MagicManaType> manaTypes, final MagicCondition conditions[], final int weight) {
        this.manaTypes=manaTypes;
        this.conditions=conditions;
        this.weight=weight;
    }
    
    public final List<MagicManaType> getManaTypes() {
        return manaTypes;
    }    
        
    public final MagicCondition[] getConditions() {
        return conditions;
    }
    
    public final int getWeight() {
        return weight;
    }
    
    public final boolean canPlay(final MagicGame game,final MagicSource source) {
        for (final MagicCondition condition : conditions) {
            if (!condition.accept(source)) {
                return false;
            }
        }
        return true;
    }
    
    public abstract MagicEvent[] getCostEvent(final MagicSource source);
    
    @Override
    public void change(MagicCardDefinition cdef) {
        cdef.addManaAct(this);
    }
}
