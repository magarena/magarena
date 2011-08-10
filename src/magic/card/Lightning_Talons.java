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

public class Lightning_Talons {

	public static final MagicSpellCardEvent V6510 =new MagicPlayAuraEvent("Lightning Talons",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
}
