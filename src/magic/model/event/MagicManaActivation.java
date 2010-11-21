package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

public abstract class MagicManaActivation {
	
	private final MagicManaType manaTypes[];
	private final MagicCondition conditions[];
	private final int weight;

	public MagicManaActivation(final MagicManaType manaTypes[],final MagicCondition conditions[],final int weight) {
		
		this.manaTypes=manaTypes;
		this.conditions=conditions;
		this.weight=weight;
	}
	
	public final MagicManaType[] getManaTypes() {
		
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
			
			if (!condition.accept(game,source)) {
				return false;
			}
		}
		
		return true;
	}
	
	public abstract MagicEvent[] getCostEvent(final MagicSource source);
}