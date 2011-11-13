package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;

public class Token_Madness_DeckGenerator extends DefaultDeckGenerator {

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
	
	public Token_Madness_DeckGenerator() {
		super(null);
		setCubeDefinition(CubeDefinitions.getInstance().getCubeDefinition(getColorText()));
	}
	
	public String getColorText() {
		return colorText;
	}
	
	public int getMinRarity() {
		return 2;
	}
	
	public boolean acceptPossibleSpellCard(MagicCardDefinition card) {
		return !card.isCreature() || card.hasText("token");
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		addRequiredCards(deck, spells);
	}
	
	public void addRequiredLands(MagicCondensedDeck deck) {
		addRequiredCards(deck, lands);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
}
