package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicSubType;

public class Vampire_Rage_DeckGenerator extends DefaultDeckGenerator {

    private static final String colorText = "br";
    private static final String[] spells = {
        "Falkenrath Marauders",
        "Falkenrath Noble",
        "Markov Patrician",
        "Markov Patrician",
        "Rakish Heir",
        "Rakish Heir",
        "Rakish Heir",
        "Stromkirk Noble",
        "Stromkirk Noble",
        "Stromkirk Noble",
        "Doom Blade",
        "Go for the Throat",
        "Go for the Throat",
        "Mask of Avacyn"
    };

    private static final String[] lands = {
        "Blackcleave Cliffs",
        "Blackcleave Cliffs",
        "Blackcleave Cliffs",
        "Blackcleave Cliffs",
        "Blood Crypt",
        "Blood Crypt",
        "Blood Crypt",
        "Dragonskull Summit",
        "Dragonskull Summit",
        "Dragonskull Summit",
        "Dragonskull Summit"
    };

    public Vampire_Rage_DeckGenerator() {
        super(null);
        setCubeDefinition(CubeDefinitions.getCubeDefinition(getColorText()));
    }

    public String getColorText() {
        return colorText;
    }

    public int getMinRarity() {
        return 2;
    }

    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        return !card.isCreature() || card.hasSubType(MagicSubType.Vampire);
    }

    public void addRequiredSpells(final MagicCondensedDeck deck) {
        addRequiredCards(deck, spells);
    }

    public void addRequiredLands(final MagicCondensedDeck deck) {
        addRequiredCards(deck, lands);
    }

    public void setColors(final MagicPlayerProfile profile) {
        profile.setColors(getColorText());
    }
}
