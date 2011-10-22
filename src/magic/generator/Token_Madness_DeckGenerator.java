package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;

public class Token_Madness_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "wg";
	
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
		String[] cards = {"Hero of Bladehold", "Hero of Bladehold", "Hero of Bladehold", "Blade Splicer", "Blade Splicer", "Intangible Virtue", "Intangible Virtue", "Intangible Virtue", "Intangible Virtue", "Midnight Haunting", "Midnight Haunting", "Oblivion Ring", "Oblivion Ring", "Timely Reinforcements", "Timely Reinforcements", "Day of Judgment", "Day of Judgment", "Vital Splicer", "Vital Splicer"};
		addRequiredCards(deck, cards);
	}
	
	public void addRequiredLands(MagicCondensedDeck deck) {
		String[] cards = {"Razorverge Thicket", "Razorverge Thicket", "Razorverge Thicket", "Razorverge Thicket", "Sunpetal Grove", "Sunpetal Grove", "Sunpetal Grove", "Sunpetal Grove"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
}
