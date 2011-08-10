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

public class Torpor_Dust {

	public static final MagicSpellCardEvent V6530 =new MagicPlayAuraEvent("Torpor Dust",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(3,0));
}
