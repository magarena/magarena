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

public class Spiritmonger {

	public static final MagicPermanentActivation V1856 =        new MagicRegenerationActivation("Spiritmonger",MagicManaCost.BLACK);

	public static final MagicPermanentActivation V1858 = new MagicPermanentActivation(			"Spiritmonger",
            new MagicCondition[]{MagicManaCost.GREEN.getCondition()},
			new MagicActivationHints(MagicTiming.Pump,false,1),
            "Color") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicColorChoice.BLUE_RED_WHITE_INSTANCE,
                    new Object[]{source},
                    this,
                    "Spiritmonger becomes the color$ of your choice until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicSetTurnColorAction(permanent,(MagicColor)choiceResults[0]));
			game.doAction(new MagicPlayAbilityAction(permanent));
		}
	};
	
    public static final MagicTrigger V9026 =new MagicVeteranTrigger("Spiritmonger",false);

}
