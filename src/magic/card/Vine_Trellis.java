package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

import java.util.Arrays;

public class Vine_Trellis {
    public static final MagicManaActivation V1 = 
        new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless, MagicManaType.Green), 1);
}
