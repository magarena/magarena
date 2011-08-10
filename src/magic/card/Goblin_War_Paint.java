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

public class Goblin_War_Paint {

	public static final MagicSpellCardEvent V6504 =new MagicPlayAuraEvent("Goblin War Paint",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicHasteTargetPicker.getInstance());
}
