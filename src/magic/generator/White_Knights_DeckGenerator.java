package magic.generator;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicSubType;

public class White_Knights_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "w";
    private static final String[] cards = {
        "Knight Exemplar",
        "Knight Exemplar",
        "Knight Exemplar",
        "Knight Exemplar",
        "Day of Judgment",
        "Student of Warfare",
        "Student of Warfare",
        "Sun Titan",
        "Kinsbaile Cavalier",
        "Honor of the Pure",
        "Honor of the Pure",
        "Honor of the Pure",
        "Hero of Bladehold",
        "Hero of Bladehold"
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
        return !card.isCreature() || card.hasSubType(MagicSubType.Knight);
    }

    @Override
    public void addRequiredSpells(final MagicCondensedDeck deck) {
        addRequiredCards(deck, cards);
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
