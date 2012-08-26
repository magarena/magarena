package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;

public class Saint___Hero_DeckGenerator extends DefaultDeckGenerator {

    private static final String colorText = "wu";
    private static final String[] spells = {
        "Geist of Saint Traft",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Batterskull",
        "Day of Judgment",
        "Day of Judgment",
        "Mana Leak",
        "Mana Leak",
        "Mana Leak",
        "Timely Reinforcements",
        "Timely Reinforcements",
        "Oblivion Ring",
        "Oblivion Ring",
        "Sword of Feast and Famine"
    };
        
    private static final String[] lands = {
        "Glacial Fortress",
        "Glacial Fortress",
        "Glacial Fortress",
        "Seachrome Coast",
        "Seachrome Coast",
        "Seachrome Coast"
    };
    
    public Saint___Hero_DeckGenerator() {
        super(null);
        setCubeDefinition(CubeDefinitions.getCubeDefinition(getColorText()));
    }
    
    public String getColorText() {
        return colorText;
    }
    
    public int getMinRarity() {
        return 2;
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
}
