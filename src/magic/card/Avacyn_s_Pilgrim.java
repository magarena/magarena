
package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

import java.util.Arrays;

public class Avacyn_s_Pilgrim {
    public static final MagicManaActivation M = 
        new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless, MagicManaType.White), 1);
}
