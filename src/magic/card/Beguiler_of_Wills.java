package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicType;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class Beguiler_of_Wills {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Control") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            final MagicTargetFilter targetFilter =
                    new MagicTargetFilter.MagicPowerTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE,
                    player.getNrOfPermanentsWithType(MagicType.Creature));
            final MagicTargetChoice targetChoice = 
                    new MagicTargetChoice(
                    targetFilter,true,MagicTargetHint.Negative,"target creature to gain control of");
            return new MagicEvent(
                    source,
                    player,
                    targetChoice,
                    MagicExileTargetPicker.create(),
                    this,
                    "Gain control of target creature$ with power less " +
                    "than or equal to the number of creatures you control.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    if (creature.getPower() <= event.getPlayer().getNrOfPermanentsWithType(MagicType.Creature)) {
                        game.doAction(new MagicGainControlAction(event.getPlayer(),creature));
                    }  
                }
            });
        }
    };
}
