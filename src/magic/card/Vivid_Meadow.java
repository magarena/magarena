package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicVividManaActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicVividLandTrigger;

import java.util.Arrays;
import java.util.List;

public class Vivid_Meadow {
    
    private static final List<MagicManaType> mTypes = magic.data.ManaActivationDefinitions.getVividManaTypes(MagicManaType.White); 

    public static final MagicTrigger V9958 =new MagicVividLandTrigger("Vivid Meadow");
    
    //tap for colorless or white
    public static final MagicManaActivation V1 = new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless,MagicManaType.White),0);
	
    //tap for rest of the colors
    public static final MagicManaActivation V2 = new MagicVividManaActivation(mTypes);
}
