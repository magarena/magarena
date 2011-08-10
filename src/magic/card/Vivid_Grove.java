package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicVividManaActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicVividLandTrigger;

import java.util.Arrays;
import java.util.List;

public class Vivid_Grove {
    
    private static final List<MagicManaType> mTypes = magic.data.ManaActivationDefinitions.getVividManaTypes(MagicManaType.Green); 

    public static final MagicTrigger V9954 =new MagicVividLandTrigger("Vivid Grove");
    
    //tap for colorless or green
    public static final MagicManaActivation V1 = new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless,MagicManaType.Green),0);
	
    //tap for rest of the colors
    public static final MagicManaActivation V2 = new MagicVividManaActivation(mTypes);
  
}
