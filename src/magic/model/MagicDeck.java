package magic.model;

import java.util.ArrayList;

public class MagicDeck extends ArrayList<MagicCardDefinition> {

	private static final long serialVersionUID = 1L;
	
	private String name="Deck";
	
	public void setName(final String name) {
		
		this.name=name;
	}
	
	public String getName() {
		
		return name;
	}
}