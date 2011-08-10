package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Skithiryx__the_Blight_Dragon {

	public static final MagicPermanentActivation V1791 =new MagicGainActivation(			"Skithiryx, the Blight Dragon",
            MagicManaCost.BLACK,
            MagicAbility.Haste,
            new MagicActivationHints(MagicTiming.FirstMain,false,1)
            );
	
	public static final MagicPermanentActivation V1797 =new MagicRegenerationActivation("Skithiryx, the Blight Dragon",MagicManaCost.BLACK_BLACK);
	
}
