package magic.model;

import java.util.ArrayList;
import magic.data.DeckType;
import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class MagicDeck extends ArrayList<MagicCardDefinition> {

    public static final int DEFAULT_SIZE = 40;

    private String filename="Unsaved Deck";
    private String description = "";
    private boolean isDeckValid = true;
    private long deckFileChecksum = 0;
    private DeckType deckType = DeckType.Random;

    public MagicDeck() {}

    public MagicDeck(final MagicDeck deck) {
        super(deck);
        this.filename = deck.filename;
        this.description = deck.description;
        this.deckFileChecksum = deck.deckFileChecksum;
        this.deckType = deck.deckType;
    }

    public void setContent(final MagicDeck deck) {
        clear();
        addAll(deck);
        this.filename = deck.filename;
        this.description = deck.description;
        this.deckFileChecksum = deck.deckFileChecksum;
        this.deckType = deck.deckType;
    }

    public void setFilename(final String name) {
        this.filename = name.isEmpty() ? "Unsaved deck" : name;
        System.out.println("MagicDeck.setFilename : " + filename);
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

    public boolean isValid() {
        if (!isDeckValid) {
            return false;
        } else if (this.size() == 0) {
            return false;
        } else {
            for (final MagicCardDefinition card : this) {
                if (!card.isValid()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setInvalidDeck(final String reason) {
        isDeckValid = false;
        setDescription(reason);
    }

    public int getCardCount(final MagicCardDefinition targetCard) {
        int count = 0;
        for (MagicCardDefinition card : this) {
            if (card == targetCard) {
                count++;
            }
        }
        return count;
    }

    public boolean contains(final MagicType type) {
        return this.stream().anyMatch(card -> card.hasType(type));
    }

    public void setDeckFileChecksum(long value) {
        this.deckFileChecksum = value;
        System.out.println("MagicDeck.setDeckFileChecksum : " + value);
    }

    public void setDeckType(DeckType deckType) {
        this.deckType = deckType;
        System.out.println("MagicDeck.setType : " + deckType.name());
    }

    long getDeckFileChecksum() {
        return deckFileChecksum;
    }

    DeckType getDeckType() {
        return deckType;
    }
}
