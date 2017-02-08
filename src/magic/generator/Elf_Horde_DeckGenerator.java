package magic.generator;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicSubType;

public class Elf_Horde_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "g";
    private static final String[] cards = {
        "Joraga Warcaller",
        "Joraga Warcaller",
        "Elvish Champion",
        "Elvish Champion",
        "Imperious Perfect",
        "Imperious Perfect",
        "Imperious Perfect",
        "Imperious Perfect",
        "Ezuri, Renegade Leader",
        "Llanowar Elves",
        "Llanowar Elves",
        "Llanowar Elves"
    };

    public String getColorText() {
        return colorText;
    }

    public int getMinRarity() {
        return 2;
    }

    @Override
    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        return !card.isCreature() || card.hasSubType(MagicSubType.Elf);
    }

    public void addRequiredSpells(final MagicCondensedDeck deck) {
        addRequiredCards(deck, cards);
    }

    public void setColors(final MagicDeckProfile profile) {
        profile.setColors(getColorText());
    }

    public boolean ignoreMaxCost() {
        return true;
    }
}
