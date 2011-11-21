package magic.card;

import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicVividManaActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicVividLandTrigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vivid_Crag {
    public static final List<MagicManaType> getVividManaTypes(final MagicManaType manaType) {
		final List<MagicManaType> manaTypes=new ArrayList<MagicManaType>(MagicColor.NR_COLORS-1);
		for (final MagicColor color : MagicColor.values()) {
			final MagicManaType colorManaType=color.getManaType();
			if (colorManaType != manaType) {
				manaTypes.add(colorManaType);
			}
		}
        return manaTypes;
	}

    public static final MagicTrigger T = new MagicVividLandTrigger();
	
    //tap for rest of the colors
    public static final MagicManaActivation V2 = new MagicVividManaActivation(
        getVividManaTypes(MagicManaType.Red)); 
}
