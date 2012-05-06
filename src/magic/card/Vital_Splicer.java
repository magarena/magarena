package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicRegenerateTargetPicker;

public class Vital_Splicer {
    public static final Object T = Blade_Splicer.T;
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
            		MagicManaCost.ONE.getCondition(),
            		MagicCondition.CONTROL_GOLEM_CONDITION},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Regen") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_GOLEM_YOU_CONTROL,
                    MagicRegenerateTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Regenerate target Golem$ you control.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRegenerateAction(creature));
                }
            });
        }
    };
}
