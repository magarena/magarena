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

public class White_Knights_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "w";
	
	public White_Knights_DeckGenerator() {
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
		return (!card.isCreature()) || card.hasSubType(magic.model.MagicSubType.Knight);
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Knight Exemplar", "Knight Exemplar", "Knight Exemplar", "Knight Exemplar", "Day of Judgment", "Student of Warfare", "Student of Warfare", "Sun Titan", "Kinsbaile Cavalier", "Honor of the Pure", "Honor of the Pure", "Honor of the Pure", "Hero of Bladehold", "Hero of Bladehold"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
	
	public boolean ignoreMaxCost() {
		return true;
	}
}
