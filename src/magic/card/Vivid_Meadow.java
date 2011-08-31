package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicVividManaActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicVividLandTrigger;

import java.util.Arrays;

public class Vivid_Meadow {
    public static final MagicTrigger T = new MagicVividLandTrigger();
    
    //tap for colorless or white
    public static final MagicManaActivation V1 = new MagicTapManaActivation(
            Arrays.asList(MagicManaType.Colorless,MagicManaType.White),0);
	
    //tap for rest of the colors
    public static final MagicManaActivation V2 = new MagicVividManaActivation(
        Vivid_Crag.getVividManaTypes(MagicManaType.White)); 
}
