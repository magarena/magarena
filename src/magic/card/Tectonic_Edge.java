package magic.card;

import magic.model.*;
import magic.model.action.MagicDestroyAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.action.MagicPermanentAction;

import java.util.Arrays;

public class Tectonic_Edge {

    public static final MagicPermanentActivation A = new MagicPermanentActivation(
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
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
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
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                }
			});
		}
	};
        
    public static final MagicManaActivation M = new MagicTapManaActivation(
            Arrays.asList(MagicManaType.Colorless),0);
}
