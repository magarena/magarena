package magic.generator;

import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;

public class Saint___Hero_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "wu";
    private static final String[] spells = {
        "Geist of Saint Traft",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Batterskull",
        "Day of Judgment",
        "Day of Judgment",
        "Mana Leak",
        "Mana Leak",
        "Mana Leak",
        "Timely Reinforcements",
        "Timely Reinforcements",
        "Oblivion Ring",
        "Oblivion Ring",
        "Sword of Feast and Famine"
    };

    private static final String[] lands = {
        "Glacial Fortress",
        "Glacial Fortress",
        "Glacial Fortress",
        "Seachrome Coast",
        "Seachrome Coast",
        "Seachrome Coast"
    };

    public String getColorText() {
        return colorText;
    }

    @Override
    public int getMinRarity() {
        return 2;
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
}
