package magic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicCondensedDeck extends ArrayList<MagicCondensedCardDefinition> {

	private static final long serialVersionUID = 143L;
	
	private String name = "Unsaved Deck";

    public MagicCondensedDeck() {}

    public MagicCondensedDeck(final MagicCondensedDeck deck) {
        super(deck);
        name = deck.getName();
    }
	
	public MagicCondensedDeck(final MagicDeck list) {
		this((List<MagicCardDefinition>) list);
		
		name = list.getName();
	}
	
	public MagicCondensedDeck(final List<MagicCardDefinition> list) {
		super();
		
		Collections.sort(list, MagicCardDefinition.NAME_COMPARATOR_DESC);
	
		MagicCondensedCardDefinition lastDeckCard = null;
		
		for(int i = 0; i < list.size(); i++) {
			// increment copies count if more than one of the same card
			if(lastDeckCard != null && MagicCardDefinition.NAME_COMPARATOR_DESC.compare(lastDeckCard.getCard(), list.get(i)) == 0) {
				lastDeckCard.incrementNumCopies();
			} else {
				lastDeckCard = new MagicCondensedCardDefinition(list.get(i));
				add(lastDeckCard);
			}
		}
	}

    public void setContent(final MagicCondensedDeck deck) {
        clear();
        addAll(deck);
        name = deck.getName();
    }
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public MagicDeck toMagicDeck() {
		MagicDeck deck = new MagicDeck();
		
		for(int i = 0; i < size(); i++) {
			MagicCondensedCardDefinition card = get(i);
			
			for(int j = 0; j < card.getNumCopies(); j++) {
				deck.add(card.getCard());
			}
		}
		
		return deck;
	}
}
