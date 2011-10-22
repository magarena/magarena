package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;

public class White_Metal_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "w";
	
	public White_Metal_DeckGenerator() {
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
		return !card.isCreature() || card.isArtifact();
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Hero of Bladehold", "Hero of Bladehold", "Hero of Bladehold", "Hero of Bladehold", "Oblivion Ring", "Memnite", "Memnite", "Mikaeus, the Lunarch", "Mirran Crusader", "Signal Pest", "Signal Pest", "Signal Pest", "Glint Hawk Idol", "Glint Hawk Idol", "Glint Hawk Idol", "Glint Hawk Idol", "Mox Opal", "Mox Opal", "Leonin Relic-Warder"};
		addRequiredCards(deck, cards);
	}
	
	public void addRequiredLands(MagicCondensedDeck deck) {
		String[] cards = {"Inkmoth Nexus", "Inkmoth Nexus", "Inkmoth Nexus", "Inkmoth Nexus"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
	
	public boolean ignoreMaxColorless() {
		return true;
	}
}
