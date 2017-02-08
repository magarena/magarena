package magic.generator;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;

public class Token_Madness_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "wg";
    private static final String[] spells = {
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Blade Splicer",
        "Blade Splicer",
        "Intangible Virtue",
        "Intangible Virtue",
        "Intangible Virtue",
        "Intangible Virtue",
        "Midnight Haunting",
        "Midnight Haunting",
        "Oblivion Ring",
        "Oblivion Ring",
        "Timely Reinforcements",
        "Timely Reinforcements",
        "Day of Judgment",
        "Day of Judgment",
        "Vital Splicer",
        "Vital Splicer"
    };

    private static final String[] lands = {
        "Razorverge Thicket",
        "Razorverge Thicket",
        "Razorverge Thicket",
        "Razorverge Thicket",
        "Sunpetal Grove",
        "Sunpetal Grove",
        "Sunpetal Grove",
        "Sunpetal Grove"
    };

    public String getColorText() {
        return colorText;
    }

    public int getMinRarity() {
        return 2;
    }

    @Override
    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        return !card.isCreature() || card.hasText("token");
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
}
