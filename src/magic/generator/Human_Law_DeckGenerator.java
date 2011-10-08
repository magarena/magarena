package magic.generator;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicColoredType;
import magic.model.MagicCondensedDeck;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRandom;

import java.util.ArrayList;
import java.util.List;

public class Human_Law_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "w";
	
	public Human_Law_DeckGenerator() {
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
		return (!card.isCreature()) || card.hasSubType(magic.model.MagicSubType.Human);
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Champion of the Parish", "Champion of the Parish", "Champion of the Parish", "Elite Vanguard", "Gideon's Lawkeeper", "Hero of Bladehold", "Hero of Bladehold", "Hero of Bladehold", "Mirran Crusader", "Mirran Crusader", "Angelic Destiny", "Angelic Destiny", "Honor of the Pure", "Honor of the Pure", "Day of Judgment"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
}
