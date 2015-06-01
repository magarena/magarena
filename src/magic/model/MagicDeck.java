package magic.model;

import java.util.ArrayList;
import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class MagicDeck extends ArrayList<MagicCardDefinition> {

    public static final int DEFAULT_SIZE = 40;

    private String filename="Unsaved Deck";
    private String description = "";
    private boolean isDeckValid = true;
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
}
