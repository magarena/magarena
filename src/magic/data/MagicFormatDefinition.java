package magic.data;

import java.util.ArrayList;
import java.util.List;
import magic.model.MagicCardDefinition;

public class MagicFormatDefinition {

    private final List<String> bannedCardNames = new ArrayList<String>();
    private final List<String> restrictedCardNames = new ArrayList<String>();
    private final List<MagicSets> magicSets = new ArrayList<MagicSets>();

    public boolean contains(MagicCardDefinition card) {
        if (bannedCardNames.contains(card.getName())) {
            return false;
        } else {
            for (MagicSets magicSet : magicSets) {
                if (MagicSetDefinitions.isCardInSet(card, magicSet)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addBannedCardName(String cardName) {
        bannedCardNames.add(cardName);
    }

    /**
     * a card that a player may only include one of in a deck or sideboard in the Vintage format.
     * (not currently used but could become relevant in future).
     */
    public void addRestrictedCardName(String cardName) {
        restrictedCardNames.add(cardName);
    }

    public void addSetCode(String line) {
        magicSets.add(MagicSets.valueOf(line));
    }

}
