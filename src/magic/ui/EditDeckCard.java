package magic.ui;

import magic.model.MagicCubeDefinition;
import magic.model.MagicDeckCard;
import magic.model.MagicPlayerDefinition;
import magic.ui.viewer.DeckViewer;

public class EditDeckCard {

	private final DeckViewer deckViewer;
	private final MagicPlayerDefinition player;
	private final MagicCubeDefinition cube;
	private final MagicDeckCard deckCard;
	
	public EditDeckCard(final DeckViewer deckViewer,final MagicPlayerDefinition player,final MagicCubeDefinition cube,final MagicDeckCard deckCard) {
		
		this.deckViewer=deckViewer;
		this.player=player;
		this.cube=cube;
		this.deckCard=deckCard;
	}
	
	public DeckViewer getDeckViewer() {
		
		return deckViewer;
	}
	
	public MagicPlayerDefinition getPlayer() {
		
		return player;
	}
	
	public MagicCubeDefinition getCube() {
		
		return cube;
	}
	
	public MagicDeckCard getDeckCard() {
		
		return deckCard;
	}
}