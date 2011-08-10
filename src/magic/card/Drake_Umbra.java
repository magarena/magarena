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

public class Drake_Umbra {

	public static final MagicSpellCardEvent V6486 =new MagicPlayAuraEvent("Drake Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
}
