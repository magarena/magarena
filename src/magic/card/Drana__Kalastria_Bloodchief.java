package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicWeakenTargetPicker;

public class Drana__Kalastria_Bloodchief {
    public static final MagicPermanentActivation A =new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.X_BLACK_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.X_BLACK_BLACK)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE,
                    new MagicWeakenTargetPicker(0,amount),
                    new Object[]{source,amount},
                    this,
                    "Target creature$ gets -0/-"+amount+" until end of turn and " + 
                    source + " gets +"+amount+"/+0 until end of turn.");
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            final int amount=(Integer)data[1];
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,0,-amount));
                }
            });
            game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],amount,0));
        }
    };
}
