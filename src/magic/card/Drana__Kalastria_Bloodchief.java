package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicWeakenTargetPicker;

public class Drana__Kalastria_Bloodchief {
    public static final MagicPermanentActivation A =new MagicPermanentActivation(
            new MagicCondition[]{MagicConditionFactory.ManaCost("{X}{B}{B}")},
            new MagicActivationHints(MagicTiming.Removal),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{X}{B}{B}"))};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                new MagicWeakenTargetPicker(0,amount),
                amount,
                this,
                "Target creature$ gets -0/-"+amount+" until end of turn and " + 
                "SN gets +"+amount+"/+0 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefInt();
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,0,-amount));
                    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),amount,0));
                }
            });
        }
    };
}
