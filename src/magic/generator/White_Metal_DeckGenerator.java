package magic.generator;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;

public class White_Metal_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "w";
    private static final String[] spells = {
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Oblivion Ring",
        "Memnite",
        "Memnite",
        "Mikaeus, the Lunarch",
        "Mirran Crusader",
        "Signal Pest",
        "Signal Pest",
        "Signal Pest",
        "Glint Hawk Idol",
        "Glint Hawk Idol",
        "Glint Hawk Idol",
        "Glint Hawk Idol",
        "Mox Opal",
        "Mox Opal",
        "Leonin Relic-Warder"
    };

    private static final String[] lands = {
        "Inkmoth Nexus",
        "Inkmoth Nexus",
        "Inkmoth Nexus",
        "Inkmoth Nexus"
    };

    public String getColorText() {
        return colorText;
    }

    public int getMinRarity() {
        return 2;
    }

    @Override
    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        return !card.isCreature() || card.isArtifact();
    }

    public void addRequiredSpells(final MagicCondensedDeck deck) {
        addRequiredCards(deck, spells);
    }

    public void addRequiredLands(final MagicCondensedDeck deck) {
        addRequiredCards(deck, lands);
    }

    public void setColors(final MagicDeckProfile profile) {
        profile.setColors(getColorText());
    }

    public boolean ignoreMaxColorless() {
        return true;
    }
}
