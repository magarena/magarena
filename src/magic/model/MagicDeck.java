package magic.model;

import java.util.ArrayList;

public class MagicDeck extends ArrayList<MagicCardDefinition> {

	private static final long serialVersionUID = 1L;
	
	private String name="Unsaved Deck";
	private String description;

    public MagicDeck() {}

    public MagicDeck(final MagicDeck deck) {
        super(deck);
        name = deck.name;
    }

    public void setContent(final MagicDeck deck) {
        clear();
        addAll(deck);
        name = deck.name;
    }
	
	public void setName(final String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}
}
