package magic.data;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;

import java.util.Set;

public class MagicLegality {

    private Set<MagicFormat> deckFormats = MagicFormat.ALL_FORMATS();
    private Set<MagicFormat> cardFormats = MagicFormat.ALL_FORMATS();
    private Set<MagicFormat> deckIllegalFormats;
    private Set<MagicFormat> cardIllegalFormats;
    private Set<MagicFormat> formatFilter= MagicFormat.ALL_FORMATS();
    
    public Set<MagicFormat> MagicLegality (final MagicDeck deck) {                               
        deckIllegalFormats.clear();                                 //Not needed?
        for (MagicCardDefinition card:deck) {                   //for each card in the deck
            for (MagicFormat cardFormat:MagicLegality(card)) {  //for each format of the card
                formatFilter.remove(cardFormat);                //remove that format from a list of all formats
            }
            deckIllegalFormats.addAll(formatFilter);                //whatever's left is illegal format for that card
        }
        deckFormats.removeAll(deckIllegalFormats);                  //All illegal formats removed from list of all formats
        return deckFormats;                                     //gives all the legal formats for the deck
    }
    
    public Set<MagicFormat> MagicLegality (final MagicCard card) {
        return MagicLegality(card.getCardDefinition());
    }
    
    public Set<MagicFormat> MagicLegality (final MagicCardDefinition card) {
        cardIllegalFormats.clear();
        
        //Card format check logic here - example of poss banned lists:

/*      for (MagicFormat format:MagicFormat.ALL_FORMATS()) {
            if (format.BannedList().contains(card)) {
                cardIllegalFormats.add(format);
            }
        }
        cardFormats.removeAll(cardIllegalFormats);*/
        
        return cardFormats;
    }
    
}