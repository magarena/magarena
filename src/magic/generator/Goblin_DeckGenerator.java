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

public class Goblin_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "r";
	
	public Goblin_DeckGenerator() {
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
		return (!card.isCreature()) || card.hasSubType(magic.model.MagicSubType.Goblin);
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Wort, Boggart Auntie", "Siege-Gang Commander", "Siege-Gang Commander", "Frenzied Goblin", "Frenzied Goblin", "Goblin Piledriver"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
	
	public boolean ignoreMaxCost() {
		return true;
	}
}
