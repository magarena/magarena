package magic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import magic.data.DeckType;
import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class MagicDeck extends ArrayList<MagicCardDefinition> {

    public static final int DEFAULT_SIZE = 40;

    private String filename = "";
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
        this.filename = name.trim();
    }

    public String getFilename() {
        return filename.isEmpty() ? "New deck" : filename;
    }

    public String getName() {
        return DeckUtils.getDeckNameFromFilename(getFilename());
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
        } else if (this.isEmpty()) {
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
    }

    public void setDeckType(DeckType deckType) {
        this.deckType = deckType;
    }

    public long getDeckFileChecksum() {
        return deckFileChecksum;
    }

    public DeckType getDeckType() {
        return deckType;
    }

    public boolean isUnsaved() {
        return deckFileChecksum == 0;
    }

    public void setUnsavedStatus() {
        deckFileChecksum = 0;
        if (deckType != DeckType.Random) {
            deckType = DeckType.Random;
        }
    }

    public boolean isSameDeckFile(MagicDeck other) {
        return this.deckFileChecksum == other.deckFileChecksum;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj != null && obj instanceof MagicDeck) {
            final MagicDeck other = (MagicDeck) obj;
            if (!this.filename.equals(other.filename)) {
                return false;
            }
            if (!this.description.equals(other.description)) {
                return false;
            }
            if (this.deckFileChecksum != other.deckFileChecksum) {
                return false;
            }
            if (this.deckType != other.deckType) {
                return false;
            }
            if (this.size() != other.size()) {
                return false;
            }
            Collections.sort(this, MagicCardDefinition.NAME_COMPARATOR_ASC);
            Collections.sort(other, MagicCardDefinition.NAME_COMPARATOR_ASC);
            return super.equals(other);
        }
        return false;
    }

    /**
     * Skips any invalid cards in deck.
     */
    private List<MagicCardDefinition> getValidCards() {
        return this.stream()
            .filter(card -> card.isValid())
            .collect(Collectors.toList());
    }

    /**
     *  Shuffles a copy of the deck and returns the first {@code count}
     *  cards or all cards, whichever is smaller.
     */
    public List<MagicCardDefinition> getRandomCards(int count) {
        List<MagicCardDefinition> cards = getValidCards();
        Collections.shuffle(cards, new MagicRandom(MagicRandom.nextRNGInt()));
        return cards.subList(0, Math.min(count, size()));
    }

    /**
     * Returns the deck name with a type prefix
     * eg. "Firemind top decks / legacy.Merfolk"
     */
    public String getQualifiedName() {
        return getDeckType() != DeckType.Random
            ? getDeckType().toString() + "  /  " + getName()
            : getName();
    }

    public boolean isNotValid() {
        return !isValid();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }
}
