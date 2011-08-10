package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicVividManaActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicVividLandTrigger;

import java.util.Arrays;
import java.util.List;

public class Vivid_Marsh {

    private static final List<MagicManaType> mTypes = magic.data.ManaActivationDefinitions.getVividManaTypes(MagicManaType.Black); 

    public static final MagicTrigger V9956 =new MagicVividLandTrigger("Vivid Marsh");
    
    //tap for colorless or black
    public static final MagicManaActivation V1 = new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless,MagicManaType.Black),0);
	
    //tap for rest of the colors
    public static final MagicManaActivation V2 = new MagicVividManaActivation(mTypes);
}
