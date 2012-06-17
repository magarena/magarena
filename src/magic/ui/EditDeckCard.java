package magic.ui;

import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerDefinition;
import magic.ui.viewer.DeckViewer;

public class EditDeckCard {

    private final DeckViewer deckViewer;
    private final MagicPlayerDefinition player;
    private final MagicCubeDefinition cube;
    private final MagicCardDefinition card;
    
    public EditDeckCard(
            final DeckViewer deckViewer,
            final MagicPlayerDefinition player,
            final MagicCubeDefinition cube,
            final MagicCardDefinition card) {
        this.deckViewer=deckViewer;
        this.player=player;
        this.cube=cube;
        this.card=card;
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
    
    public MagicCardDefinition getCard() {
        return card;
    }
}
