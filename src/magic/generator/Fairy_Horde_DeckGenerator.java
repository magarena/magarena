package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicSubType;

public class Fairy_Horde_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "bu";
	
	public Fairy_Horde_DeckGenerator() {
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
		return !card.isCreature() || card.hasSubType(MagicSubType.Faerie);
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Scion of Oona", "Scion of Oona", "Scion of Oona", "Scion of Oona", "Bitterblossom", "Bitterblossom", "Bitterblossom", "Terror", "Damnation", "Mistbind Clique", "Mistbind Clique", "Mistbind Clique"};
		addRequiredCards(deck, cards);
	}
	
	public void addRequiredLands(MagicCondensedDeck deck) {
		String[] cards = {"Mutavault", "Mutavault", "Mutavault", "Mutavault", "Creeping Tar Pit", "Watery Grave", "Watery Grave"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
	
	public boolean ignoreMaxCost() {
		return true;
	}
}
