package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;

public class White_Metal_DeckGenerator extends DefaultDeckGenerator {

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

	public White_Metal_DeckGenerator() {
		super(null);
		setCubeDefinition(CubeDefinitions.getCubeDefinition(getColorText()));
	}
	
	public String getColorText() {
		return colorText;
	}
	
	public int getMinRarity() {
		return 2;
	}
	
	public boolean acceptPossibleSpellCard(MagicCardDefinition card) {
		return !card.isCreature() || card.isArtifact();
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
	
	public boolean ignoreMaxColorless() {
		return true;
	}
}
