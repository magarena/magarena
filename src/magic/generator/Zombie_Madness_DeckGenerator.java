package magic.generator;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicSubType;

public class Zombie_Madness_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "b";
    private static final String[] cards = {
        "Cemetery Reaper",
        "Cemetery Reaper",
        "Cemetery Reaper",
        "Cemetery Reaper",
        "Death Baron",
        "Death Baron",
        "Festering Goblin",
        "Festering Goblin",
        "Lord of the Undead",
        "Lord of the Undead",
        "Lord of the Undead",
        "Call to the Grave",
        "Call to the Grave",
        "Severed Legion"
    };

    public String getColorText() {
        return colorText;
    }

    public int getMinRarity() {
        return 2;
    }

    @Override
    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        return !card.isCreature() || card.hasSubType(MagicSubType.Zombie);
    }

    public void addRequiredSpells(final MagicCondensedDeck deck) {
        addRequiredCards(deck, cards);
    }

    public void setColors(final MagicDeckProfile profile) {
        profile.setColors(getColorText());
    }
}
