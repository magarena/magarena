package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicSubType;

public class Fairy_Horde_DeckGenerator extends DefaultDeckGenerator {

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
		addRequiredCards(deck, spells);
	}
	
	public void addRequiredLands(MagicCondensedDeck deck) {
		addRequiredCards(deck, lands);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
	
	public boolean ignoreMaxCost() {
		return true;
	}
}
