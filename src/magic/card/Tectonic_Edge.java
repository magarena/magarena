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

public class Tectonic_Edge {

    public static final MagicPermanentActivation V3169 =new MagicPermanentActivation("Tectonic Edge",
			new MagicCondition[]{
                MagicManaCost.TWO.getCondition(),  //add ONE for the card itself
                MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.OPP_FOUR_LANDS_CONDITION
            },
			new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
			    new MagicTapEvent((MagicPermanent)source),
			    new MagicSacrificeEvent((MagicPermanent)source),
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_NONBASIC_LAND,
                    new MagicDestroyTargetPicker(false),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target nonbasic land$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};
        
    public static final MagicManaActivation V1 = new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless),0);
}
