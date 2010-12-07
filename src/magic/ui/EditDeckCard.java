package magic.ui;

import magic.model.MagicDeckCard;
import magic.model.MagicPlayerDefinition;
import magic.ui.viewer.DeckViewer;

public class EditDeckCard {

	private final DeckViewer deckViewer;
	private final MagicPlayerDefinition player;
	private final MagicDeckCard deckCard;
	
	public EditDeckCard(final DeckViewer deckViewer,final MagicPlayerDefinition player,final MagicDeckCard deckCard) {
		
		this.deckViewer=deckViewer;
		this.player=player;
		this.deckCard=deckCard;
	}
	
	public DeckViewer getDeckViewer() {
		
		return deckViewer;
	}
	
	public MagicPlayerDefinition getPlayer() {
		
		return player;
	}
	
	public MagicDeckCard getDeckCard() {
		
		return deckCard;
	}
}