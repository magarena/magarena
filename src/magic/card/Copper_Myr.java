
package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

import java.util.Arrays;

public class Copper_Myr {
    public static final MagicManaActivation M = 
        new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless, MagicManaType.Green), 1);
}
