package magic.data.generator;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckGenerator;
import magic.model.MagicCardDefinition;
import magic.model.MagicColoredType;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRandom;

import java.util.ArrayList;
import java.util.List;

public class KnightDeckGenerator extends DeckGenerator {

	private final String colorText = "w";
	
	public KnightDeckGenerator() {
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
	
	public void addRequiredCards(MagicDeck deck) {
		String reqCardNames[] = {"Knight Exemplar", "Knight Exemplar", "Knight Exemplar", "Day of Judgment", "Student of Warfare", "Student of Warfare", "Sun Titan", "Kinsbaile Cavalier", "Honor of the Pure", "Honor of the Pure", "Honor of the Pure", "Hero of Bladehold"};
		
		for(String name : reqCardNames) {
			final MagicCardDefinition cardDef = CardDefinitions.getInstance().getCard(name);
			if (cardDef.isValid()) {
				deck.add(cardDef);
            } else {
				System.out.println("cannot find " + name);
			}
		}
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
	
	public boolean ignoreMaxCost() {
		return true;
	}
}
