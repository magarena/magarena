package magic.model;

import java.util.HashSet;

public class MagicCubeDefinition extends HashSet<String> {

	private static final long serialVersionUID = 1L;
	
	private final String name;
	
	public MagicCubeDefinition(final String name) {
		this.name=name;
	}
	
	public boolean containsCard(final MagicCardDefinition cardDefinition) {
		return contains(cardDefinition.getName());
	}
	
	public String getName() {
		return name + " (" + size() + " cards)";
	}
	
	@Override
	public String toString() {
		return name;
	}
}