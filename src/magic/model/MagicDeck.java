package magic.model;

import java.util.ArrayList;

import magic.data.DeckUtils;

public class MagicDeck extends ArrayList<MagicCardDefinition> {

    private static final long serialVersionUID = 1L;

    private String filename="Unsaved Deck";
    private String description;

    public MagicDeck() {}

    public MagicDeck(final MagicDeck deck) {
        super(deck);
        filename = deck.filename;
    }

    public void setContent(final MagicDeck deck) {
        clear();
        addAll(deck);
        filename = deck.filename;
        description = deck.description;
    }

    public void setFilename(final String name) {
        this.filename = name;
    }
    public String getFilename() {
        return filename;
    }

    public String getName() {
        return DeckUtils.getDeckNameFromFilename(filename);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String desc) {
        this.description = desc;
    }

    @Override
    public void clear() {
        super.clear();
        this.description = "";
    }
}
