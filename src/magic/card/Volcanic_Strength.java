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

public class Volcanic_Strength {

	public static final MagicSpellCardEvent V6534 =new MagicPlayAuraEvent("Volcanic Strength",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
}
