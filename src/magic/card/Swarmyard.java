package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicRegenerateTargetPicker;

public class Swarmyard {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Pump,false),
            "Regen") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicTapEvent(source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL,
                MagicRegenerateTargetPicker.getInstance(),
                this,
                "Regenerate target Insect, Rat, Spider, or Squirrel$."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRegenerateAction(creature));
                }
            });
        }
    };
}
