package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;

public class Vampire_Rage_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "br";
	
	public Vampire_Rage_DeckGenerator() {
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
		return (!card.isCreature()) || card.hasSubType(magic.model.MagicSubType.Vampire);
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Falkenrath Marauders", "Falkenrath Noble", "Markov Patrician", "Markov Patrician", "Rakish Heir", "Rakish Heir", "Rakish Heir", "Stromkirk Noble", "Stromkirk Noble", "Stromkirk Noble", "Doom Blade", "Go for the Throat", "Go for the Throat", "Mask of Avacyn"};
		addRequiredCards(deck, cards);
	}
	
	public void addRequiredLands(MagicCondensedDeck deck) {
		String[] cards = {"Blackcleave Cliffs", "Blackcleave Cliffs", "Blackcleave Cliffs", "Blackcleave Cliffs", "Blood Crypt", "Blood Crypt", "Blood Crypt", "Dragonskull Summit", "Dragonskull Summit", "Dragonskull Summit", "Dragonskull Summit"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
}
