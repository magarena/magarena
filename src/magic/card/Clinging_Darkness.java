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

public class Clinging_Darkness {

	public static final MagicSpellCardEvent V6484 =new MagicPlayAuraEvent("Clinging Darkness",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(-4,-1));
}
