package magic.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicPlayerProfile;
import magic.model.MagicSubType;

public class Tribal_Mono_DeckGenerator extends DefaultDeckGenerator {

	private static final int MIN_NUM_CARDS_WITH_SUBTYPE = 30;
	
	private static final Random randGen = new Random();
	// all possible tribes - calculated once
	private static final ArrayList<MagicSubType> possibleTribes = new ArrayList<MagicSubType>();
	private static final ArrayList<ArrayList<String>> possibleColors = new ArrayList<ArrayList<String>>();
	
	// random tribe from all possible for each instance
	private final MagicSubType tribe;
	private final String colorText;
	
	public Tribal_Mono_DeckGenerator() {
		super(null);
		
		if(!hasChoice()) {
			getPossibleTribes();
		}
		
		if(hasChoice()) {
			int i = randGen.nextInt(possibleTribes.size());
			tribe = possibleTribes.get(i);
			colorText = possibleColors.get(i).get(randGen.nextInt(possibleColors.get(i).size()));
		} else {
			tribe = null;
			colorText = "";
		}
		
		setCubeDefinition(CubeDefinitions.getInstance().getCubeDefinition(getColorText()));
	}
	
	private boolean hasChoice() {
		return possibleTribes.size() > 0 && possibleColors.size() == possibleTribes.size();
	}
	
	private void getPossibleTribes() {
        for(MagicSubType s : MagicSubType.ALL_CREATURES) {
        	HashMap<MagicColor, Integer> countColors = new HashMap<MagicColor, Integer>();
        	countColors.put(MagicColor.Black, new Integer(0));
        	countColors.put(MagicColor.White, new Integer(0));
        	countColors.put(MagicColor.Green, new Integer(0));
        	countColors.put(MagicColor.Red, new Integer(0));
        	countColors.put(MagicColor.Blue, new Integer(0));
        	
        	// count colors
        	for(MagicCardDefinition card : CardDefinitions.getCards()) {
        		if(card.hasSubType(s)) {
        			int colorFlags = card.getColorFlags();
        			
        			for(MagicColor c : countColors.keySet()) {
        				if (c.hasColor(colorFlags)) {
        					countColors.put(c, new Integer(countColors.get(c).intValue() + 1));
        				}
        			}
        		}
        	}
        	
        	ArrayList<String> choiceColors = getPossibleColors(countColors); 
        	
        	if(choiceColors.size() > 0) {
        		possibleTribes.add(s);
        		possibleColors.add(choiceColors);
        	}
        }
	}
	
	private ArrayList<String> getPossibleColors(HashMap<MagicColor, Integer> countColors) {
		// monocolor
		ArrayList<String> a = new ArrayList<String>();
		
		for(MagicColor c : countColors.keySet()) {
			if(countColors.get(c).intValue() > MIN_NUM_CARDS_WITH_SUBTYPE) {
				a.add("" + c.getSymbol());
			}
		}
		
		return a;
	}
	
	public String getColorText() {
		return colorText;
	}
	
	public int getMinRarity() {
		return 1;
	}
	
	public boolean acceptPossibleSpellCard(MagicCardDefinition card) {
		if(hasChoice()) {
			return !card.isCreature() || card.hasSubType(tribe);
		} else {
			return true;
		}
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
	
	public boolean ignoreMaxCost() {
		return false;
	}
}
