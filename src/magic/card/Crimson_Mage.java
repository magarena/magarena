package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicHasteTargetPicker;

public class Crimson_Mage {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
        new MagicCondition[]{MagicConditionFactory.ManaCost("{R}")},
        new MagicActivationHints(MagicTiming.Pump,true),
        "Haste"
    ) {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{R}"))};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicHasteTargetPicker.create(),
                this,
                "Target creature$ you control gains haste until end of turn."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
                }
            });
        }
    };
}
