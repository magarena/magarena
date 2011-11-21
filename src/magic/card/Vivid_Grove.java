package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicVividManaActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicVividLandTrigger;

public class Vivid_Grove {
    public static final MagicTrigger T = new MagicVividLandTrigger();
    
    //tap for rest of the colors
    public static final MagicManaActivation V2 = new MagicVividManaActivation(
        Vivid_Crag.getVividManaTypes(MagicManaType.Green)); 
  
}
