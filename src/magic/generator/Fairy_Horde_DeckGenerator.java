package magic.generator;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicSubType;

public class Fairy_Horde_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "bu";
    private static final String[] spells = {
        "Scion of Oona",
        "Scion of Oona",
        "Scion of Oona",
        "Scion of Oona",
        "Bitterblossom",
        "Bitterblossom",
        "Bitterblossom",
        "Terror",
        "Damnation",
        "Mistbind Clique",
        "Mistbind Clique",
        "Mistbind Clique"
    };

    private static final String[] lands = {
        "Mutavault",
        "Mutavault",
        "Mutavault",
        "Mutavault",
        "Creeping Tar Pit",
        "Watery Grave",
        "Watery Grave"
    };

    public String getColorText() {
        return colorText;
    }

    @Override
    public int getMinRarity() {
        return 2;
    }

    @Override
    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        return !card.isCreature() || card.hasSubType(MagicSubType.Faerie);
    }

    @Override
    public void addRequiredSpells(final MagicCondensedDeck deck) {
        addRequiredCards(deck, spells);
    }

    @Override
    public void addRequiredLands(final MagicCondensedDeck deck) {
        addRequiredCards(deck, lands);
    }

    @Override
    public void setColors(final MagicDeckProfile profile) {
        profile.setColors(getColorText());
    }

    @Override
    public boolean ignoreMaxCost() {
        return true;
    }
}
